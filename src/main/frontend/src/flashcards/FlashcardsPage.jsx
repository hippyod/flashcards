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
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import axios from 'axios';

import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';

import Layout from '../Layout';
import Flashcard from './Flashcard';

const styles = theme => ({
    layout: {
        display: "block", // Fix IE11 issue.
        marginLeft: theme.spacing.unit * 3,
        marginRight: theme.spacing.unit * 3,
        [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
            width: 400,
            marginLeft: "auto",
            marginRight: "auto",
        },
    },
    paper: {
        marginTop: theme.spacing.unit * 3,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: `${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
        backgroundColor: '#ddd',
    },
});

class FlashcardsPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    constructor(props) {
        super(props);

        this.nextCard = this.nextCard.bind(this);
    }

    state = {
        flashcardType: {
            flashcards: [],
        },
        cardIndex: 0,
    };

    componentDidMount() {
        axios({
            headers: {
                Authorization: 'Bearer ' + sessionStorage.jwtToken,
            },
            url: '/flashcardTypes/shuffled/' + this.props.type,
        })
        .then(response => {
            this.setState({ flashcardType: response.data });
            return response;
        })
        .catch(response => {
            console.log(response);
        });
    }

    nextCard() {
        const cardIndex =  this.state.cardIndex + 1;
        this.setState({ cardIndex: cardIndex});
    }

    render() {
        const { classes } = this.props;

        let flashcard = this.state.flashcardType.flashcards[this.state.cardIndex];

        return (
            (this.state.flashcardType.flashcards.length > 0) ?
                <Layout title={this.state.flashcardType.title + ' Flash Cards'} >
                    <div className={classes.layout}>
                        <Paper className={classes.paper}>
                            <Flashcard flashcardId={flashcard.id}
                                       type={flashcard.type}
                                       question={this.state.flashcardType.question}
                                       answer={flashcard.answer}
                                       userAnswerFormat={this.state.flashcardType.userAnswerFormat}
                                       class={flashcard.question.replace(' ', '_')}
                                       nextCard={this.nextCard}/>
                        </Paper>
                    </div>
                </Layout>
                : null

        );
    }
}

export default withStyles(styles)(FlashcardsPage);
