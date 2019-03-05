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

import { withStyles } from "@material-ui/core/styles";
import IconButton from "@material-ui/core/IconButton";
import InputAdornment from "@material-ui/core/InputAdornment";
import TextField from '@material-ui/core/TextField';

import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';


const styles = theme => ({
});

class PasswordField extends Component {
    static defaultProps = {
        endAdornment: false,
    }

    constructor(props) {
        super(props);

        this.handleOnChange = this.handleOnChange.bind(this);
    }

    handleClickShowPassword = () => {
        if (typeof this.props.onVisibleToggleClick === 'function') {
            this.props.onVisibleToggleClick();
        }
    };

    handleOnChange(event) {
        try {
            this.props.onChange(event);
        }
        catch (err) {
            // ignore if not set
        }
    };

    render() {
        return (
            <TextField name={this.props.name}
                       value={this.props.value}
                       type={this.props.showPassword ? 'text' : 'password'}
                       fullWidth={this.props.fullWidth}
                       label={this.props.label}
                       required={true}
                       onChange={ this.handleOnChange }
                       helperText={this.props.errorMessage}
                       error={this.props.error}
                       InputProps={{
                            endAdornment:
                                this.props.endAdornment &&
                                <InputAdornment position="end">
                                    <IconButton aria-label="Toggle password visibility"
                                                onClick={ this.handleClickShowPassword }>
                                        {this.props.showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>,
                        }}
            />
        );
    }
}

export default withStyles(styles)(PasswordField);
