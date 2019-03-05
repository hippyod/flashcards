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
import Paper from '@material-ui/core/Paper';

import Layout from '../Layout';
import UserResultsTable from './UserResultsTable';

const styles = theme => ({
    layout: {
        display: "block", // Fix IE11 issue.
        marginLeft: theme.spacing.unit * 3,
        marginRight: theme.spacing.unit * 3,
        [theme.breakpoints.up(400 + theme.spacing.unit * 3 * 2)]: {
            minWidth: 500,
            maxWidth: 800,
            marginLeft: "auto",
            marginRight: "auto",
        },
    },
    paper: {
        marginTop: theme.spacing.unit * 1.5,
        marginBottom: theme.spacing.unit * 1.5,
        flexDirection: "column",
        alignItems: "center",
        padding: `${theme.spacing.unit * 2}px ${theme.spacing.unit * 2}px`,
    },
    tableWrapper: {
        overflowX: 'auto',
    },
});

class UserResultsPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    render() {
        const { classes } = this.props;
        const title = this.props.location.state.title;

        return (
            <Layout title={title + ' Flash Cards Results'} >
                <div className={classes.layout}>
                    <Paper className={classes.paper}>
                        <div className={classes.tableWrapper}>
                            <UserResultsTable type={this.props.type}/>
                        </div>
                    </Paper>
                </div>
            </Layout>
        );
    }
}

export default withStyles(styles)(withRouter(UserResultsPage));
