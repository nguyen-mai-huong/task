import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Login from '../src/userModule/pages/Login';
import Dashboard from './userModule/pages/Dashboard';
import Signup from './userModule/pages/Signup';
import SingPassCallback from './userModule/pages/SingPassCallback';

const App = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/">
          <Login/>
        </Route>
        <Route path="/dashboard">
          <Dashboard />
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