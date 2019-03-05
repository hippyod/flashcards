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
import React from 'react';

import { withStyles } from "@material-ui/core/styles";
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Slide from '@material-ui/core/Slide';

import UserAnswerService, { UserAnswerStyles } from '../service/UserAnswerService';

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

class FlashcardResultDialog extends React.Component {

    getTitle(result) {
        switch (result) {
            case UserAnswerService.WARNING: return 'Almost... (We\'ll give it to you)';
            case UserAnswerService.CORRECT: return 'CORRECT!';
            default: return 'INCORRECT!';
        }
    }

    getMessage(result) {
        switch (result) {
            case UserAnswerService.CORRECT: return 'Excellent work!';
            default: return <span>Correct Answer: <q>{this.props.answer}</q><br/>Your answer: <q>{this.props.userAnswer}</q></span>;
        }
    }

    render() {
        const { classes } = this.props;

        let result = UserAnswerService.evaluateAnswer(this.props.answer, this.props.userAnswer);

        let title = this.getTitle(result);
        let message = this.getMessage(result);

        return (
            <div>
                <Dialog open={this.props.open}
                        TransitionComponent={Transition}
                        keepMounted
                        onClose={this.props.onClose}
                        aria-labelledby="alert-dialog-slide-title"
                        aria-describedby="alert-dialog-slide-description" >
                    <DialogTitle id="alert-dialog-slide-title" className={ classes[result] }><div className={ classes[result] }>{title}</div></DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-slide-description"><br/>{message}</DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button variant="outlined" onClick={this.props.onClose}  >
                            Next Card
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(UserAnswerStyles)(FlashcardResultDialog);
