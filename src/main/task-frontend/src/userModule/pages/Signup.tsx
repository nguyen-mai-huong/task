import { ChangeEvent, FormEvent, useState } from 'react';
import config from '../../configs/config';
import message from '../../configs/message';

import httpClient from '../../httpClient';
import { Redirect } from 'react-router-dom';

import useStyles from '../../style/styles';
import { Box, Button, CssBaseline, Grid, Paper, TextField, Typography } from '@material-ui/core';
import Copyright from '../../components/Copyright';

const Signup = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [isValidUsername, setIsValidUsername] = useState(true);
  const [isValidPassword, setIsValidPassword] = useState(true);
  const [isValidSignup, setIsValidSignup] = useState(false);

  const classes = useStyles();

  const handleUsername = (event: ChangeEvent<HTMLInputElement>): void => {
    setIsValidUsername(true);
    setUsername(event.target.value);
  }

  const handlePassword = (event: ChangeEvent<HTMLInputElement>): void => {
    setIsValidPassword(true);
    setPassword(event.target.value);
  }

  const handleEmail = (event: ChangeEvent<HTMLInputElement>): void => {
    setEmail(event.target.value);
  }

  const validate = (): boolean => {
    let isValid: boolean = true;

    if (!validateUsername()) {
      setIsValidUsername(false);
      isValid = false;
    }

    if (!validatePassword()) {
      setIsValidPassword(false);
      isValid = false;
    }

    return isValid;
  }

  const validateUsername = (): boolean => {
    if (username.length < config.username.minLength) {
      return false;
    }

    const upperCasePattern: string = `[A-Z]{${config.username.minUpperCase},}`;
    const hasMinUpperCase: boolean = validateField(upperCasePattern, username);

    const lowerCasePattern: string = `[a-z]{${config.username.minLowerCase},}`;
    const hasMinLowerCase: boolean = validateField(lowerCasePattern, username);

    const specialCharPattern: string = `[!|@|#|$|%|^|&]{${config.username.minSpecialChar},}`;
    const hasMinSpecialChar: boolean = validateField(specialCharPattern, username);

    const numericValuePattern: string = `[\\d]{${config.username.minNumericVal},}`;
    const hasMinNumericValue: boolean = validateField(numericValuePattern, username);

    return hasMinUpperCase && hasMinLowerCase && hasMinSpecialChar && hasMinNumericValue;   
  }

  const validatePassword = (): boolean => {
    if (password.length < config.password.minLength) {
      return false;
    }

    const upperCasePattern: string = `[A-Z]{${config.password.minUpperCase},}`;
    const hasMinUpperCase: boolean = validateField(upperCasePattern, password);

    const lowerCasePattern: string = `[a-z]{${config.password.minLowerCase},}`;
    const hasMinLowerCase: boolean = validateField(lowerCasePattern, password);

    const specialCharPattern: string = `[!|@|#|$|%|^|&]{${config.password.minSpecialChar},}`;
    const hasMinSpecialChar: boolean = validateField(specialCharPattern, password);

    const numericValuePattern: string = `[\\d]{${config.password.minNumericVal},}`;
    const hasMinNumericValue: boolean = validateField(numericValuePattern, password);

    return hasMinUpperCase && hasMinLowerCase && hasMinSpecialChar && hasMinNumericValue;        
  }

  const validateField = (fieldPattern: string, fieldValue: string): boolean => {
    const fieldRegex = new RegExp(fieldPattern, 'g');
    if (fieldValue.match(fieldRegex)) {
      return true;
    }
    return false;
  }

  const handleSignup = (event: FormEvent): void => {
    event.preventDefault();

    if (!validate()) {
      return;
    }

    const params = {
      username: username,
      password: password,
      email: email
    }

    httpClient.post('/user/signup', params).then((response) => {
      console.log("Response: ", response);
      const data = response.data;
      if (data.code === 'ok') {
        setIsValidSignup(true);
      }

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }

  let usernameErrorMessage: string = isValidUsername ? '' : `${message.login.usernameErrorMessage} ${config.username.minLength} characters, 
                                        ${config.username.minLowerCase} lower case(s), ${config.username.minUpperCase} upper case(s),
                                        ${config.username.minSpecialChar} special character(s), ${config.username.minNumericVal} numeric value(s)`;

  let passwordErrorMessage: string = isValidPassword ? '' : `${message.login.passwordErrorMessage} ${config.password.minLength} characters,
                                        ${config.password.minLowerCase} lower case(s), ${config.password.minUpperCase} upper case(s),
                                        ${config.password.minSpecialChar} special character(s), ${config.password.minNumericVal} numeric value(s)`;

  if (isValidSignup) {
    return <Redirect to="/" />
  }

  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid item xs={false} sm={4} md={7} className={classes.image} />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <div className={classes.paper}>
          <Typography component="h1" variant="h5">
            Singapore Examinations and Assessment Board
          </Typography>
          <form className={classes.form}>
            <TextField 
              variant="outlined"
              fullWidth
              margin="normal"
              label="Username"
              type="text"
              value={username}
              onChange={handleUsername}
              error={!isValidUsername}
              helperText={usernameErrorMessage}
              required                
            />
            <TextField 
              variant="outlined" 
              fullWidth   
              margin="normal"            
              label="Password"
              type="password"   
              value={password}
              onChange={handlePassword}
              error={!isValidPassword}
              helperText={passwordErrorMessage}
              required             
            />
            <TextField 
              variant="outlined" 
              fullWidth   
              margin="normal"            
              label="Email"
              type="text"   
              value={email}
              onChange={handleEmail}
              required             
            />            
            <Button
              variant="contained"
              color="primary"
              type="submit"
              fullWidth 
              onClick={handleSignup}
            >
              Sign up
            </Button>
            <Box mt={5}>
              <Copyright />
            </Box>
          </form>
        </div>
      </Grid>
    </Grid>
  )
} 

export default Signup;