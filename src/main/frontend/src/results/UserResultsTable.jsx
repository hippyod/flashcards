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

import classNames from 'classnames';

import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableFooter from '@material-ui/core/TableFooter';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';

import UserResultsTableActions from './UserResultsTableActions';
import UserAnswerService, { UserAnswerStyles } from '../service/UserAnswerService';

const styles = theme => ({
    root: {
        width: '100%',
        marginTop: theme.spacing.unit * 3,
    },
    table: {
        minWidth: 500,
    },
    tableHeader: {
        fontSize: 16,
        textAlign: 'center',
        color: theme.palette.primary.dark,
        borderBottom: 'ridge',
    },
    tableCell: {
        fontSize: 14,
        whiteSpace: 'nowrap',
    },
    userAnswer: {
        borderRadius: 15,
        padding: 2,
    },
    tableWrapper: {
        overflowX: 'auto',
    },
});

class UserResultsTable extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    state = {
        results: [],
        page: 0,
        rowsPerPage: 10,
        totalResults: 0,
        totalPages: 0,
        dateOptions: { year: 'numeric', month: 'short', day: 'numeric' },
    };

    constructor(props) {
        super(props);

        this.handleChangePage = this.handleChangePage.bind(this);
        this.handleChangeRowsPerPage = this.handleChangeRowsPerPage.bind(this);
    }

    componentDidMount() {
        this.getPage(this.state.page, this.state.rowsPerPage)
    }

    handleChangePage(event, page) {
        this.getPage(page, this.state.rowsPerPage)
    };

    handleChangeRowsPerPage(event) {
        let page = Math.floor((this.state.page * this.state.rowsPerPage) / event.target.value)
        this.getPage(page, event.target.value);
    };

    getPage(page, rowsPerPage) {
        let config = {
            params: { page: page, size: rowsPerPage, },
        }
        axios.get('/user/results/' + this.props.type, config)
        .then(response => {
            this.setState({
                results: response.data.content,
                page: response.data.number,
                rowsPerPage: rowsPerPage,
                totalResults: response.data.totalElements,
                totalPages: response.data.totalPages,
            });
            return response;
        });
    }

    getRows() {
        const { classes } = this.props;

        let results = this.state.results;
        let lastDate = null;
        return (
            results.map( (result, index) => {
                let thisDate = new Date(result.dateCreated);
                let borderTop = (lastDate && thisDate.getDay() !== lastDate.getDay()) ? {borderTop: 'double'} : null;
                lastDate = thisDate;

                let formattedDate =  thisDate.toLocaleDateString('en-US', this.state.dateOptions);
                let answerState = UserAnswerService.evaluateAnswer(result.flashcard.answer, result.userAnswer);

                return (
                    <TableRow hover={true} key={index} style={borderTop}>
                        <TableCell variant="body" className={classes.tableCell} >{result.flashcard.question}</TableCell>
                        <TableCell variant="body" className={classes.tableCell}>{result.flashcard.answer}</TableCell>
                        <TableCell variant="body" className={classes.tableCell}>
                            <div className={classNames(classes[answerState], classes.userAnswer)}>{result.userAnswer}</div>
                        </TableCell>
                        <TableCell variant="body" className={classes.tableCell}>{ formattedDate }</TableCell>
                    </TableRow>
                )
            })
        );
    }

    render() {
        const { classes } = this.props;
        const { results, rowsPerPage, totalResults } = this.state;
        const emptyRows = rowsPerPage - results.length;

        return (
            <div className={classes.tableWrapper}>
                <Table className={classes.table}>
                    <TableHead>
                        <TableRow>
                            <TableCell variant="head" className={classes.tableHeader}>Question</TableCell>
                            <TableCell variant="head" className={classes.tableHeader}>Answer</TableCell>
                            <TableCell variant="head" className={classes.tableHeader}>User Answer</TableCell>
                            <TableCell variant="head" className={classes.tableHeader}>Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        { this.getRows() }
                        {emptyRows > 0 &&
                            <TableRow style={{ height: 48 * emptyRows }}>
                                <TableCell colSpan={4} />
                            </TableRow>
                        }
                    </TableBody>
                    <TableFooter>
                        <TableRow>
                            <TableCell></TableCell>
                            <TablePagination
                                colSpan={3}
                                count={totalResults}
                                rowsPerPage={rowsPerPage}
                                rowsPerPageOptions={[10, 25, 50]}
                                labelRowsPerPage={'Results per page:'}
                                page={this.state.page}
                                onChangePage={this.handleChangePage}
                                onChangeRowsPerPage={this.handleChangeRowsPerPage}
                                ActionsComponent={UserResultsTableActions}
                            />
                        </TableRow>
                    </TableFooter>
                </Table>
            </div>
        );
    }
}

export default withStyles(UserAnswerStyles)(withStyles(styles)(UserResultsTable));
