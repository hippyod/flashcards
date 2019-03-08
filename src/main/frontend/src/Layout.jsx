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
import classnames from 'classnames';

import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';

import ExpandMoreIcon from '@material-ui/icons/ExpandMore';

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
    icon: {
       padding: 0,
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
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
        expanded: true,
    };

    handleExpandClick = () => {
        this.setState(state => ({ expanded: !state.expanded }));
    };

    render() {
        const { classes } = this.props;

        return (
            <div>
                <FlashcardAppBar title={this.props.title} />
                {this.props.children}
                <br />
                <footer className={classes.footer}>
                    <IconButton
                        className={classnames(classes.expand, { [classes.expandOpen]: this.state.expanded, }, classes.icon)}
                        onClick={this.handleExpandClick}
                        aria-expanded={this.state.expanded}
                        aria-label="Show more"
                    >
                        <ExpandMoreIcon />
                        <Typography variant="body2" color="textSecondary" component="p" width="10">
                            Copyright &#xa9;2019 Evan "Hippy" Slatis &#9679; hippyod - at - yahoo - dot - com
                        </Typography>
                    </IconButton>
                    <Collapse in={this.state.expanded} timeout="auto" unmountOnExit>
                        <Typography variant="caption" color="textSecondary" component="p" width="10">
                            This is a non-commerical, learning demo application for use by anyone.
                            No third party cookies or advertising is used on this site.
                            <br/>
                            The code is OSS under the Apache 2.0 license and found here: https://github.com/hippyod/flashcards<br/>
                            No personal information whatsoever is collected or asked for.  Have fun.
                            </Typography>
                        <Typography variant="body2" color="textSecondary" component="p" width="10">
                            Copyright &#xa9;2019 Evan "Hippy" Slatis &#9679; hippyod - at - yahoo - dot - com
                        </Typography>
                    </Collapse>
                </footer>
            </div>
        );
    }
}

export default withStyles(styles)(Layout);