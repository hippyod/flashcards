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
import Grid from '@material-ui/core/Grid';

import Layout from '../Layout';
import FlashcardType from './FlashcardType';

const styles = theme => ({
    layout: {
        width: 'auto',
        marginTop: theme.spacing.unit * 3,
        [theme.breakpoints.up(1100 + theme.spacing.unit)]: {
            maxWidth: 975,
            marginLeft: 'auto',
            marginRight: 'auto',
        },
        backgroundColor: '#ddd',
    },
});

class FlashcardTypesPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    state = {
        flashcardTypes: [],
    };

    componentDidMount() {
        axios.get('/flashcardTypes')
        .then(response => {
            this.setState({ flashcardTypes: response.data });
            return response;
        })
        .catch(error => {
            console.log(error);
        });
    }

    render() {
        const { classes } = this.props;

        const flashcardTypes = this.state.flashcardTypes.map((flashcardType, index) => {
            return (
                <Grid key={index} item>
                    <FlashcardType type={flashcardType.type} title={flashcardType.title} description={flashcardType.description} />
                </Grid>
            );
        });

        return (
            <Layout title="Choose Your Flash Cards" >
                <Paper className={classes.layout}>
                    <Grid container justify="space-evenly" spacing={16}>
                        {flashcardTypes}
                    </Grid>
                </Paper>
            </Layout>
        );
    }
}

export default withStyles(styles)(FlashcardTypesPage);
