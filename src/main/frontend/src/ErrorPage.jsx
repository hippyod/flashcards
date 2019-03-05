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

import { withRouter } from 'react-router';

import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';

import FlashcardAppBar from './FlashcardAppBar';

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
        padding: `${theme.spacing.unit * 3}px ${theme.spacing.unit * 3}px`,
        backgroundColor: '#ddd',
    },
    card: {
        maxWidth: 345,
    },
    media: {
        height: 240,
    },
});

class ErrorPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    render() {
        const { classes } = this.props;

        return (
            <div>
                <FlashcardAppBar title={'Whoops!'} />
                <div className={classes.layout}>
                    <Paper className={classes.paper}>
                        <Card raised={true} className={classes.card} onClick={this.props.history.goBack}>
                            <CardActionArea>
                                <CardMedia className={classes.media} image="./images/uhoh.png" title="Contemplative Reptile" />
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="h2">
                                        Whoops!
                                    </Typography>
                                    <Typography component="p">We don't know how you got here or what went wrong, so...</Typography>
                                    <Typography variant="button">go back?</Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                    </Paper>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(withRouter(ErrorPage));
