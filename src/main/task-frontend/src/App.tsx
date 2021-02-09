import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Login from '../src/userModule/pages/Login';
import Home from '../src/userModule/pages/Home';
import Signup from './userModule/pages/Signup';
import SingPassCallback from './userModule/pages/SingPassCallback';

const App = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          <Login/>
        </Route>
        <Route path="/home">
          <Home />
        </Route>
        <Route path="/signup">
          <Signup />
        </Route>
        <Route path="/singpass/callback">
          <SingPassCallback />
        </Route>
      </Switch>
    </BrowserRouter>
  )
}

export default App;