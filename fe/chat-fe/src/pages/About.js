import React, { Component } from 'react';
import {Card} from 'antd'

class About extends Component {
    state = {  }
    render() { 
        return ( 
            <div style={{ padding: 24 }}>
                <Card bordered={false} hoverable style={{ marginBottom: 24 }} bodyStyle={{ minHeight: 130 }}>
                    <div className="markdown">
                        <h3>关于</h3>
                        广告位招租~~
                    </div>
                </Card>
            </div>
         );
    }
}
 
export default About;