import { useReducer } from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import Signup from "./components/Signup";
import Home from "./components/Home";
import Login from "./components/Login";
import SingPassCallback from "./components/SingPassCallback";

function loginReducer(state, action) {
  switch(action.type) {
    case 'UPDATE_IS_LOGINED':
      console.log("Payload is: ", action, action.payload);
      return {...state, isLogined: action.payload};
    default:
      return state;
  }

}

function App() {

  // define useReducer
  const initialState = {isLogined: false};
  const [state, dispatch] = useReducer(loginReducer, initialState);
  

  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          <Login 
            isLogined = {state.isLogined}
            updateIsLogined = {(payload) => dispatch({type: 'UPDATE_IS_LOGINED', payload: payload})}
          />
        </Route>
        <Route path="/signup">
          <Signup 
            isLogined = {state.isLogined}
          />
        </Route>
        <Route path="/home">
          <Home 
            isLogined = {state.isLogined}
            updateIsLogined = {(payload) => dispatch({type: 'UPDATE_IS_LOGINED', payload: payload})}
          />
        </Route>
        <Route path="/singpass/callback">
          <SingPassCallback 
            isLogined = {state.isLogined}
            updateIsLogined = {(payload) => dispatch({type: 'UPDATE_IS_LOGINED', payload: payload})}
          />
        </Route>
      </Switch>
    </BrowserRouter>
  )

}


export default App;