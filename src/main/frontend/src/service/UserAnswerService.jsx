import jaroWinkler from 'talisman/metrics/distance/jaro-winkler';

const ERROR = 'ERROR';
const WARNING = 'WARNING';
const CORRECT = 'CORRECT';

const styles = {
    ERROR: {
        backgroundColor: '#f00',
        color: '#fff',
        textAlign: 'center',
    },
    WARNING: {
        backgroundColor: '#ff0',
        color: '#000',
        textAlign: 'center',
    },
    CORRECT: {
        backgroundColor: '#0f0',
        color: '#fff',
        textAlign: 'center',
    },
}

class UserAnswerService {
    static evaluateAnswer(answer, userAnswer) {
        if (Number.isInteger(answer)) {
            userAnswer = userAnswer.padStart(2, '0');
        }

        let upperAnswer = answer.toUpperCase();
        let upperUserAnswer = userAnswer.toUpperCase();
        let jwDistance = jaroWinkler(upperAnswer, upperUserAnswer);

        let result = ERROR;
        if (jwDistance === 1) {
            result = CORRECT;
        } else if (jwDistance >= 0.85) {
            result = WARNING;
        }
        return result;
    }

    static get WARNING() {
        return WARNING;
    }
    
    static get CORRECT() {
        return CORRECT;
    }

    static get ERROR() {
        return ERROR;
    }
}

export { UserAnswerService as default, styles as UserAnswerStyles };
