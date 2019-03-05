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

import axios from 'axios';

import { withRouter } from 'react-router'
import { Route, Switch, Redirect } from 'react-router-dom';

import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import blue from '@material-ui/core/colors/blue';
import deepPurple from '@material-ui/core/colors/deepPurple';
import grey from '@material-ui/core/colors/grey';
import CssBaseline from '@material-ui/core/CssBaseline';

import './App.css';
import LoginPage from "./login/LoginPage";
import FlashcardTypesPage from "./flashcards/FlashcardTypesPage";
import FlashcardsStudyPage from "./flashcards/FlashcardsStudyPage";
import FlashcardsPage from "./flashcards/FlashcardsPage";
import UserResultsPage from "./results/UserResultsPage";
import ErrorPage from "./ErrorPage";

// A theme with custom primary and secondary color.
// It's optional.
const theme = createMuiTheme({
    palette: {
        primary: blue,
        secondary: deepPurple,
        background: {
            default: grey[300]
        },
    },
    typography: {
        useNextVariants: true,
    },
});

if (window.performance) {
    if (performance.navigation.type === 1) {
        window.location = '/refresh';
    }
}

class App extends Component {

    constructor(props) {
        super(props);

        this.logout = this.logout.bind(this);
    }

    componentDidMount() {
        const { history } = this.props
        axios.defaults.headers.common['Accept'] = 'application/json';
        axios.defaults.headers.post['Content-Type'] = 'application/json';

        axios.interceptors.request.use(config => {
            if (window.sessionStorage.jwtToken) {
                config.headers.Authorization = 'Bearer ' + window.sessionStorage.jwtToken;
            }
            return config;
        }, function (error) {
            history.replace('/error');
            return Promise.reject(error);
        });

        axios.interceptors.response.use(response => {
            return response;
        }, function (error) {
            if (error.response.status === 401) {
                delete window.sessionStorage.jwtToken;
                axios.get('/logout');
                if (window.location.pathname !== '/') {
                    window.location.href = '/';
                }
            }
            else {
                history.replace('/error');
            }
            return Promise.reject(error);
        });
    }

    logout() {
        delete window.sessionStorage.jwtToken;
        axios.get('/logout');
        return (
            <Redirect to="/" />
        );
    }


    getApplicationRouting() {
        let component = null;
        if (sessionStorage.jwtToken) {
            component = (
                <Switch>
                    <Route path="/error" component={ErrorPage} />} />
                    <Route path="/logout" render={this.logout} />
                    <Route path="/login" render={() => <Redirect to="/flashcardTypes" />} />
                    <Route exact path="/flashcardTypes" render={props => <FlashcardTypesPage {...props} appContext={this} />} />
                    <Route path="/flashcards/study/:type" render={props => <FlashcardsStudyPage {...props} type={props.match.params.type} appContext={this} />} />
                    <Route path="/flashcards/results/:type" render={props => <UserResultsPage {...props} type={props.match.params.type} appContext={this} />} />
                    <Route path="/flashcards/:type" render={props => <FlashcardsPage {...props} type={props.match.params.type} appContext={this} />} />
                    <Route path="/images" />
                    <Route path="/*" render={() => <Redirect to="/flashcardTypes" />} />
                </Switch>
            );
        }
        else {
            component = (
                <Switch>
                    <Route exact path="/" component={LoginPage} />} />
                    <Route path="/" render={() => <Redirect to="/" />} />
                </Switch>
            );
        }

        return component;
    }

    render() {
        const page = this.getApplicationRouting()
        return (
            <MuiThemeProvider theme={theme}>
                <CssBaseline />
                <div className="App">{page}</div>
            </MuiThemeProvider>
        );
    }
}

export default withRouter(App);
