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

import { withStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

import FlashcardAppBar from './FlashcardAppBar';

const styles = theme => ({
    footer: {
        position: "fixed",
        top: 'auto',
        bottom: 0,
        width: "100%",
        padding: "2px 7px 2px 0px",
        backgroundColor: '#eee',
    },
});

class Layout extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    constructor(props) {
        super(props);
    }

    state = {
    };

    render() {
        const { classes } = this.props;

        return (
            <div>
                <FlashcardAppBar title={this.props.title}/>
                {this.props.children}
                <br/>
                <footer className={classes.footer}>
                    <Typography variant="subtitle1" align="right" color="textSecondary" component="p" width="10">
                        &#xa9;2019 Evan "Hippy" Slatis
                    </Typography>
                </footer>
            </div>
        );
    }
}

export default withStyles(styles)(Layout);