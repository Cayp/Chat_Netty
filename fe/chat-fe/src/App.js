import React from 'react';
import { Route, Link, Switch, withRouter, Redirect,  BrowserRouter as Router, } from "react-router-dom";
import { Login } from './auth/Login';
import Home from './pages/home';
import { FirstIndex } from './auth/FirstIndex';
class App extends React.Component {

  constructor(props) {
    super(props);
  
  }

  render() {
    return (
      <Router>
          <div style={{height:'100%'}}>
      <Switch>
        <Route path="/auth" component={FirstIndex}/>
        <Route path="/home" component={Home}/>
      </Switch>
      </div>
      </Router>
    )
  }
}


export default App;
