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
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import Paper from '@material-ui/core/Paper';


import Layout from '../Layout';
import Flashcard from './Flashcard';

const styles = theme => ({
    layout: {
        width: '75%',
        marginTop: theme.spacing.unit * 2,
        marginBottom: theme.spacing.unit * 2,
        padding: theme.spacing.unit,
        [theme.breakpoints.up(1000 + theme.spacing.unit)]: {
            marginLeft: 'auto',
            marginRight: 'auto',
        },
        backgroundColor: '#ddd',
    },
    gridListTile: {
        overflow: 'visible',
    }
});

class FlashcardsStudyPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    state = {
        flashcardType: {
            flashcards: [],
        },
    };

    componentDidMount() {
        axios({
            headers: {
                Authorization: 'Bearer ' + sessionStorage.jwtToken,
            },
            url: '/flashcardTypes/' + this.props.type,
        })
        .then(response => {
            let flashcardType = response.data;
            this.setState({ flashcardType: flashcardType });
            return response;
        })
        .catch(response => {
            console.log(response);
        });

        window.addEventListener('resize', () => {this.forceUpdate();});

    }

    render() {
        const { classes } = this.props;

        const flashcards = this.state.flashcardType ? this.state.flashcardType.flashcards.map((flashcard, index) => {
            return (
                <GridListTile key={index} classes={{tile: classes.gridListTile }}>
                    <Flashcard type={flashcard.type}
                               question={flashcard.question}
                               answer={flashcard.answer}
                               class={flashcard.question.replace(' ', '_')}
                               studyPage={true}/>
                </GridListTile>
            );
        }) : [];

        let maxCols = Math.ceil(this.state.flashcardType.flashcards.length / 10);
        let cols = Math.floor( ((window.innerWidth * .75 > 1000) ? 1000 : window.innerWidth * .75) / 1000 * maxCols);

        return (
            <div style={{display: (this.state.flashcardType.flashcards.length > 0) ? 'inherit' : 'none'}} >
                <Layout title={this.state.flashcardType.title + ' Study Page'} >
                    <Paper className={classes.layout}>
                        <GridList justify="center" cols={cols} cellHeight="auto" spacing={8}>
                            {flashcards}
                        </GridList>
                    </Paper>
                </Layout>
            </div>
        );
    }
}

export default withStyles(styles)(FlashcardsStudyPage);
