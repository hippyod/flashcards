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

import { Link } from 'react-router-dom';

import { withStyles } from "@material-ui/core/styles";
import AppBar from '@material-ui/core/AppBar';
import Fab from "@material-ui/core/Fab";
import Button from "@material-ui/core/Button";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";

const styles = theme => ({
    flex: {
        marginLeft: 10,
        textAlign: 'left',
        flexGrow: 1,
    },
});

class FlashcardAppBar extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    render() {
        const { classes } = this.props;

        return (
            <AppBar color="primary" position="sticky">
                <Toolbar>
                    <Tooltip title="Home">
                        <Link to="/" >
                            <Fab style={{ backgroundImage: 'url(/logo.png)', backgroundPosition: 'center', backgroundSize:'cover', }} color={"primary"} >
                                <div style={{ display: 'none'}} />
                            </Fab>
                        </Link>
                    </Tooltip>
                    <Typography variant="h6" color="inherit" className={classes.flex}>
                        {this.props.title}
                    </Typography>
                    {
                        window.sessionStorage.jwtToken &&
                        <Tooltip title="Logout">
                            <Link to="/logout" >
                                <Button size="small"  ><div style={{color: '#fff'}}>Logout</div></Button>
                            </Link>
                        </Tooltip>
                    }
                </Toolbar>
            </AppBar>
        );
    }
}

export default withStyles(styles)(FlashcardAppBar);