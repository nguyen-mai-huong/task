import { useState } from "react";
import { Box, Button, CssBaseline, Grid, Paper, TextField, Typography } from '@material-ui/core';
import Copyright from './Copyright';
import httpClient from '../httpClient';
import { Redirect } from 'react-router-dom';
import config from "../configs/config";

import useStyles from '../styles';
import message from "../configs/message";

function Signup(props) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [isValidUsername, setIsValidUsername] = useState(true);
  const [isValidPassword, setIsValidPassword] = useState(true);
  const [isValidSignup, setIsValidSignup] = useState(false);

  const classes = useStyles();

  const handleUsername = (event) => {
    setIsValidUsername(true);
    setUsername(event.target.value);
  }

  const handlePassword = (event) => {
    setIsValidPassword(true);
    setPassword(event.target.value);
  }

  const handleEmail = (event) => {
    setEmail(event.target.email);
  }

  const validate = () => {
    if (!validateUsername()) {
      setIsValidUsername(false);
    }

    if (!validatePassword()) {
      setIsValidPassword(false);
    }

    return isValidUsername && isValidPassword;
  }

  const validateUsername = () => {
    if (username.length < config.username.minLength) {
      return false;
    }

    const upperCasePattern = `[A-Z]{${config.username.minUpperCase},}`;
    const hasMinUpperCase = validateField(upperCasePattern, username);

    const lowerCasePattern = `[a-z]{${config.username.minLowerCase},}`;
    const hasMinLowerCase = validateField(lowerCasePattern, username);

    const specialCharPattern = `[!|@|#|$|%|^|&]{${config.username.minSpecialChar},}`;
    const hasMinSpecialChar = validateField(specialCharPattern, username);

    const numericValuePattern = `[\\d]{${config.username.minNumericVal},}`;
    const hasMinNumericValue = validateField(numericValuePattern, username);

    return hasMinUpperCase && hasMinLowerCase && hasMinSpecialChar && hasMinNumericValue;    
  }

  const validatePassword = () => {
    if (password.length < config.password.minLength) {
      return false;
    }

    const upperCasePattern = `[A-Z]{${config.password.minUpperCase},}`;
    const hasMinUpperCase = validateField(upperCasePattern, password);

    const lowerCasePattern = `[a-z]{${config.password.minLowerCase},}`;
    const hasMinLowerCase = validateField(lowerCasePattern, password);

    const specialCharPattern = `[!|@|#|$|%|^|&]{${config.password.minSpecialChar},}`;
    const hasMinSpecialChar = validateField(specialCharPattern, password);

    const numericValuePattern = `[\\d]{${config.password.minNumericVal},}`;
    const hasMinNumericValue = validateField(numericValuePattern, password);

    return hasMinUpperCase && hasMinLowerCase && hasMinSpecialChar && hasMinNumericValue;        
  }

  const validateField = (fieldPattern, fieldValue) => {
    const fieldRegex = new RegExp(fieldPattern, 'g');
    return fieldValue.match(fieldRegex);
  }

  const handleSignup = (event) => {
    event.preventDefault();

    if (!validate()) {
      return false;
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

  let usernameErrorMessage = isValidUsername ? '' : `${message.login.usernameErrorMessage} ${config.username.minLength} characters, 
                                                      ${config.username.minLowerCase} lower case(s), ${config.username.minUpperCase} upper case(s),
                                                      ${config.username.minSpecialChar} special character(s), ${config.username.minNumericVal} numeric value(s)`;

  let passwordErrorMessage = isValidPassword ? '' : `${message.login.passwordErrorMessage} ${config.password.minLength} characters,
                                                      ${config.password.minLowerCase} lower case(s), ${config.password.minUpperCase} upper case(s),
                                                      ${config.password.minSpecialChar} special character(s), ${config.password.minNumericVal} numeric value(s)`;

  if (isValidSignup) {
    return <Redirect to="/" />
  }

  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid order={1} item xs={false} sm={4} md={7} className={classes.image} />
      <Grid order={2} item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
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
              margin="normal"    
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

