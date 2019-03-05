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

import axios from 'axios';

import { withStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";
import TextField from "@material-ui/core/TextField";
import Typography from "@material-ui/core/Typography";

import Layout from '../Layout';
import PasswordField from './PasswordField';

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
        marginTop: theme.spacing.unit * 8,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: `${theme.spacing.unit * 3}px ${theme.spacing.unit * 5}px ${theme.spacing.unit * 5}px`,
    },
    flex: {
        flexGrow: 1,
    },
});

class LoginPage extends Component {
    static propTypes = {
        classes: PropTypes.object.isRequired,
    };

    state = {
        formTitle: "",
        changeFormButtonTitle: "",
        changeFormButtonQuestion: "",
        showPassword: false,
        loginFormAction: "",
        isLogin: false,
        isFailedLogin: false,
        formData: {
            userName: '',
            password: '',
            confirmPassword: '',
        },
    };

    constructor(props) {
        super(props);

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleClickShowPassword = this.handleClickShowPassword.bind(this);
        this.handleTextFieldChange = this.handleTextFieldChange.bind(this);
        this.udpateLoginSignupButton = this.udpateLoginSignupButton.bind(this);
    }

    componentDidMount() {
        this.udpateLoginSignupButton();
    }

    handleClick(event) {
        this.udpateLoginSignupButton();
    }

    udpateLoginSignupButton() {
        let isLogin = !this.state.isLogin;
        let isFailedLogin = false;

        let passwordFields = [];

        let formTitle = isLogin ? "Login" : "Sign Up";
        let changeFormButtonQuestion = isLogin ? "Need to register" : "Already registered";
        let changeFormButtonTitle = isLogin ? "Sign up here" : "Login here";
        let loginFormAction = isLogin ? "/login" : "/sign-up";

        let formData = this.state.formData;
        formData.confirmPassword= '';
        this.setState({
            formTitle: formTitle,
            changeFormButtonTitle: changeFormButtonTitle,
            changeFormButtonQuestion: changeFormButtonQuestion,
            passwordFields: passwordFields,
            loginFormAction: loginFormAction,
            isLogin: isLogin,
            isFailedLogin: isFailedLogin,
            formData: formData,
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        this.setState({isFailedLogin: false});
        axios.post(this.state.loginFormAction, JSON.stringify(this.state.formData))
            .then(response => {
                if (response.headers['x-token']) {
                    window.sessionStorage.jwtToken = response.headers['x-token'];
                }
                return response;
            })
            .catch(error => {
                if (error.response.status === 401) {
                    this.setState({isFailedLogin: true});
                }
            })
            .then(() => {
                this.props.history.push('/');
            });
    }

    handleClickShowPassword(event) {
        this.setState({ showPassword: !this.state.showPassword });
    };

    handleTextFieldChange(event) {
        let formData = this.state.formData;
        formData[event.target.name] = event.target.value;
        this.setState({ formData });
    }

    isFormValid() {
        let formData = this.state.formData;
        let isFormValid = this.state.isLogin || (formData.password === formData.confirmPassword);

        isFormValid = isFormValid && (formData.userName.length >= 6 && formData.userName.length <=20);
        isFormValid = isFormValid && (formData.password.length >= 6 && formData.password.length <=20);
        isFormValid = isFormValid && (this.state.isLogin || (formData.confirmPassword.length >= 6 && formData.confirmPassword.length <=20));

        return isFormValid;
    }

    createPasswordField(isConfirmField) {
        let formData = this.state.formData;
        return (
            <PasswordField name={isConfirmField ? 'confirmPassword' : 'password'}
                           value={isConfirmField ? formData.confirmPassword : formData.password}
                           showPassword={this.state.showPassword}
                           endAdornment={!isConfirmField}
                           fullWidth={true}
                           label={isConfirmField ? 'Confirm your Password' : 'Password'}
                           onVisibleToggleClick={ this.handleClickShowPassword }
                           error={this.state.isFailedLogin || (!this.state.isLogin && (formData.password !== formData.confirmPassword))}
                           errorMessage={(isConfirmField && (formData.password !== formData.confirmPassword)) ? 'Passwords do not match' : '' }
                           onChange={ this.handleTextFieldChange } />
        );
    }

    render() {
        const { classes } = this.props;

        const isFormValid = this.isFormValid();
        const formData = this.state.formData;
        return (
            <Layout title="Welcome to Flash Cards">
                <form onSubmit={this.handleSubmit} className={classes.layout}>
                    <Paper className={classes.paper}>
                        <img src='lockIcon.png' alt="Logo" />
                        <Typography variant="h5">{this.state.formTitle}</Typography>
                        <Typography variant="subtitle1" color="error">{this.state.isFailedLogin ? 'Invalid user name or password' : ''}</Typography>
                        <br />
                        <br />
                        <TextField fullWidth={true}
                                   name="userName"
                                   value={formData.userName}
                                   required={true}
                                   autoFocus={true}
                                   error={formData.isFailedLogin}
                                   label="User Name"
                                   onChange={ this.handleTextFieldChange } />
                        <br />
                        {this.createPasswordField(false)}
                        {!this.state.isLogin && this.createPasswordField(true) }
                        <div key="submitButton">
                            <br />
                            <br />
                            <Button variant="contained" color="primary" type="submit" disabled={ !isFormValid }>
                                {this.state.formTitle}
                            </Button>
                        </div>
                        <div key="choose">
                            <br />
                            <Typography>
                                {this.state.changeFormButtonQuestion}?
                                <Button size="small" onClick={this.udpateLoginSignupButton} style={{ color: "#00f" }}>
                                    {this.state.changeFormButtonTitle}!
                                </Button>
                            </Typography>
                        </div>
                    </Paper>
                </form>
            </Layout>
        );
    }
}

export default withStyles(styles)(LoginPage);
