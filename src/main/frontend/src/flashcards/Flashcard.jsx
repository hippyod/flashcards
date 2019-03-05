/*
   Copyright 2019 Evan "Hippy" Slatis

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
import React, { Component } from "react";
import PropTypes from "prop-types";

import axios from 'axios';

import { withRouter } from 'react-router';

import classNames from 'classnames';

import injectSheet from 'react-jss/lib/injectSheet';

import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardMedia from "@material-ui/core/CardMedia";
import Input from "@material-ui/core/Input";
import Typography from "@material-ui/core/Typography";

import MULTIPLICATION_Styles from './styles/MULTIPLICATION.sprite.css.jsx';
import US_STATES_Styles from './styles/US_STATES.sprite.css';
import US_STATE_CAPITALS_Styles from './styles/US_STATE_CAPITALS.sprite.css';
import FlashcardResultDialog from './FlashcardResultDialog.jsx';

const styles = theme => ({
    card: {
        maxWidth: 345,
        backgroundColor: '#fff',
    },
    cardActionArea: {
        margin: '0 auto',
    },
    cardMedia: {
        marginTop: theme.spacing.unit,
    },
    cardContent: {
        maxWidth: 300,
        padding: 0,
    },
    cardActions: {
        margin: '0 auto',
    },
    textInput: {
        fontSize: 26,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    submitButton: {
        marginTop: '16px',
    },
    fonts: {
        fontSize: 26,
        fontWeight: 'bold',
    }
});

class Flashcard extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    static defaultProps = {
        studyPage: false,
    }

    state ={
        userAnswer: '',
        showResultDialog: false,
    }

    constructor(props) {
        super(props);

        this.handleUserAnswerChange = this.handleUserAnswerChange.bind(this);
        this.handleSubmitAnswer = this.handleSubmitAnswer.bind(this);
        this.nextCard = this.nextCard.bind(this);
    }

    handleUserAnswerChange(event) {
        let value = event.target.value;
        let userAnswer = (this.props.userAnswerFormat && value.length > 0) ? this.state.userAnswer : value;
        if (userAnswer !== value) {
            let userAnswerFormat = new RegExp('^' + this.props.userAnswerFormat + '$', 'g');
            if (userAnswerFormat.test(value)) {
                userAnswer = value;
            }
        }
        this.setState({ userAnswer: userAnswer });
    }

    handleSubmitAnswer(event) {
        axios.post(encodeURI('/flashcards/result/' + this.props.flashcardId + '/' + this.state.userAnswer.trim()))
            .then(response => {
                this.setState({ showResultDialog: true });
                return response;
            })
            .catch(error => {
                console.log(error);
            });
    }

    nextCard(event) {
        this.setState({ showResultDialog: false, userAnswer: '' });
        this.props.nextCard();
    }

    render() {
        const { classes } = this.props;

        let imageClass = this.props.studyPage ? classes[this.props.type + '_Study'] : classes[this.props.type];
        let showCardContent = this.props.studyPage || (!this.props.studyPage && this.props.question);

        return (
            <div>
                <Card className={classes.card}>
                    <CardActionArea className={classes.cardActionArea}>
                        <CardMedia
                            className={classNames(classes.cardMedia, imageClass, classes[this.props.class])}
                            image={'/images/' + this.props.type + '.sprite.png'}
                            title={this.props.question}
                        />
                        {showCardContent &&
                            <CardContent className={classes.cardContent}>
                                {this.props.studyPage &&
                                    <div>
                                        <Typography component="subheading" className={classes[this.props.type + '_Show_Question']}>
                                            {this.props.question}
                                        </Typography>
                                        <Typography component="h1" className={classes.fonts}>
                                            {this.props.answer}
                                        </Typography>
                                    </div>
                                }
                                {!this.props.studyPage &&
                                    <Typography component="h1" className={classes.fonts}>
                                        {this.props.question}
                                    </Typography>
                                }
                            </CardContent>
                        }
                    </CardActionArea>
                    <CardActions>
                        {!this.props.studyPage &&
                            <div className={classes.cardActions}>
                                <Input  value={this.state.userAnswer}
                                        classes={{ input: classes.textInput }}
                                        name="userAnswer"
                                        required={true}
                                        autoFocus={true}
                                        onChange={this.handleUserAnswerChange}
                                        style={{ width: '90%', }} />
                                <Button variant="outlined" className={classes.submitButton} onClick={this.handleSubmitAnswer}>
                                    Submit
                                </Button>
                            </div>
                        }
                    </CardActions>
                </Card>
                <FlashcardResultDialog open={this.state.showResultDialog}
                                       answer={this.props.answer}
                                       userAnswer={this.state.userAnswer.trim()}
                                       onClose={this.nextCard} />
            </div>
        );
    }
}
let comp = injectSheet(MULTIPLICATION_Styles)(Flashcard);
comp = injectSheet(US_STATES_Styles)(comp);
comp = injectSheet(US_STATE_CAPITALS_Styles)(comp);
comp = withStyles(styles)(comp);
comp = withRouter(comp);

export default comp;
