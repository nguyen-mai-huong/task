import { useReducer, useState } from "react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import { AppBar, Box, Button, Container, Grid, Paper, TextField, Typography, withStyles } from '@material-ui/core';

import httpClient from './httpClient';

// define reducer
function loginReducer(state, action) {
  switch(action.type) {
    case 'UPDATE_IS_LOGINED':
      console.log("Payload is: ", action, action.payload);
      return {...state, isLogined: action.payload};
    default:
      return state;
  }

}

function AppR() {

  // define useReducer
  const initialState = {isLogined: false};
  const [state, dispatch] = useReducer(loginReducer, initialState);
  

  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          {/* <Login />  Need to pass in dispatch function as a prop */}
          <Login 
            isLogined = {state.isLogined}
            updateIsLogined = {(payload) => dispatch({type: 'UPDATE_IS_LOGINED', payload: payload})}
          />
        </Route>
        <Route path="/home">
          <Home 
            isLogined = {state.isLogined}
          />
        </Route>
      </Switch>
    </BrowserRouter>
  )

}

function Login(props) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleUsername = (event) => {
    setUsername(event.target.value);
  }

  const handlePassword = (event) => {
    setPassword(event.target.value);
  }

  const handleLogin = (event) => {
    event.preventDefault();

    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);

    httpClient.post('/user/login', params).then((response) => {
      console.log("Response: ", response, response.request.response);
      const responseURL = response.request.responseURL;
      if (responseURL === LOGIN_SUCCESS_URL) {
        props.updateIsLogined(true);
      } else {
        props.updateIsLogined(false);

      }  

    }).catch((error) => {
      console.log("Error: ", error);
    })
  }


  // return (
  //   <div>
  //     <Button
  //       variant="contained"
  //       color="primary"
  //       type="submit"
  //       margin="normal"    
  //       onClick={handleLogin}
  //     >
  //       Log In
  //     </Button>
  //   </div>
  // );
  if (props.isLogined) {
    return <Redirect to="/home" />;
  };

  return (
    <Grid container component="main">
      <Grid order={2} item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <div>
          <Typography component="h1" variant="h5">
            Singapore Examinations and Assessment Board
          </Typography>
          <form>

            <TextField 
              variant="outlined"
              fullWidth
              margin="normal"
              label="Username"
              type="text"
              value={username}
              onChange={handleUsername}
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
              required             
            />
            <Button
              variant="contained"
              color="primary"
              type="submit"
              fullWidth
              margin="normal"    
              onClick={handleLogin}
       
            >
              Log In
            </Button>

          </form>
        </div>
      </Grid>
    </Grid>
  )


}

function Home(props) {
  if (!props.isLogined) {
    return <Redirect to="/" />
  }

  return (
    <div>Home</div>
  )
}

const LOGIN_SUCCESS_URL = 'http://localhost:8080/';

export default AppR;