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

import { Link } from 'react-router-dom';

import classNames from 'classnames';

import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';

const styles = theme => ({
    card: {
        maxWidth: 345,
        backgroundColor: '#eee',
    },
    flashcardTypeBackgroundStyle: {
        width: 300,
        height: 200,
        backgroundSize: '100% auto',
        display: 'inline-block',
        backgroundRepeat: 'no-repeat',
    },
    MULTIPLICATION: {
        backgroundPositionY: 'calc(100% / 2 * 0)',
    },
    US_STATES: {
        backgroundPositionY: 'calc(100% / 2 * 1)',
    },
    US_STATE_CAPITALS: {
        backgroundPositionY: 'calc(100% / 2 * 2)',
    },
});

class FlashcardTypesPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    render() {
        const { classes } = this.props;

        return (
            <Card className={classes.card}>
                <CardActionArea onClick={this.handleClick}>
                    <CardMedia
                        className={classNames(classes.flashcardTypeBackgroundStyle, classes[this.props.type])}
                        image="/images/flashcardTypes.sprite.png"
                        title={this.props.title}
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="h2">
                            {this.props.title}
                        </Typography>
                        <Typography component="p">{this.props.description}</Typography>
                    </CardContent>
                </CardActionArea>
                <CardActions>
                    <Link to={{ pathname: '/flashcards/' + this.props.type }}>
                        <Button size="small" color="primary">Start</Button>
                    </Link>
                    <Link to={{ pathname: '/flashcards/study/' + this.props.type }}>
                        <Button size="small" color="primary">Study</Button>
                    </Link>
                    <Link to={{ pathname: '/flashcards/results/' + this.props.type, state: { title: this.props.title }, }}>
                        <Button size="small" color="primary">Past Results</Button>
                    </Link>
                </CardActions>
            </Card>
        );
    }
}

export default withStyles(styles)(FlashcardTypesPage);
