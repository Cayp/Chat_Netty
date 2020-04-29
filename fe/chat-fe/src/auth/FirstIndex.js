import React from 'react';
import { Form, Input, Button, Checkbox, Card} from 'antd';
import { Route, Link, Switch, withRouter, Redirect, } from "react-router-dom"; 
import '../App.css'
import { Register } from './Register';
import WrappedNormalLoginForm from './Login';
export class FirstIndex extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "登录"
        }
    }
    
  

    render() {
        return (   
            <div className="login" style={{ minHeight: '100vh'}}> 
             <Card title={this.state.title} className="login-form1">
                <Switch>
                    <Route exact path={`/auth/login`} render={() => <WrappedNormalLoginForm/>}/>
                    <Route exact path={`/auth/register`} render={() => <Register/>}/>
                </Switch>
            </Card>
            </div> 
     );
    }
}
