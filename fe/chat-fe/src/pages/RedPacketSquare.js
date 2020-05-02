import React from 'react';
import { List, Card, Divider, Button, Modal, Form, Input, Radio, message} from 'antd';
import { render } from '@testing-library/react';
import { myAxios } from '../utils/myAxios';
const RadioGroup = Radio.Group;
const form = Form.create({})

@form
class RedPacketSquare extends React.Component {
    constructor(props) {
        super(props);  
        this.state = {
            redpacketList: [],
            visible: false,
            confirmLoading: false,
            redpacketType: undefined,
            redpacketMoney: undefined,
            redpacketSize: undefined
        }
    }
componentDidMount() {
    this.getRedPacketList()
}

componentDidUpdate(prevProps) {
     
}

getRedPacketList = () => {
    myAxios.get('/chat/redpacket/redpackets')
           .then((response) => {
              let json = response.data;
              if (json.code === 20000) {
                  this.setState({redpacketList: json.dataList || []})
              }            
           })
}

publishRedpacke = () => {
this.setState({visible: true})
}

handleOk = () => {
    this.setState({
      confirmLoading: true,
    });
    this.props.form.validateFields((err, values) => {
    console.log('Received values of form: ', values);
      if (!err) {
        if (values.redPacket_size > 200) {
            message.error("红包个数不能大于200")
            this.setState({ confirmLoading: false})
            return
        }
        myAxios.post('/chat/redpacket/publish', values)
               .then((response) => {
                   let json = response.data
                    this.setState({ confirmLoading: false})
                   if (json.code === 20000) {
                       message.success(json.message)
                       this.getRedPacketList()
                       this.props.form.resetFields();
                       this.setState({
                        visible: false,
                      });
                   } else {
                       message.error(json.message)
                   }
               })        
      } else {
        this.setState({ confirmLoading: false})
      }
    });
  };

  handleCancel = () => {
    this.props.form.resetFields();
    this.setState({
      visible: false,
    });
  };

render() {
    const { redpacketList, visible, confirmLoading } = this.state
    const { getFieldDecorator } = this.props.form;
    return ( 
    <div style={{ padding: 24 }}>
         <Button type="danger"onClick={this.publishRedpacke}>发红包</Button>
         <Modal
          title="编辑红包"
          visible={visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
          footer={[
            <Button key="back" onClick={this.handleCancel}>
              取消
            </Button>,
            <Button key="submit" type="danger" loading={confirmLoading} onClick={this.handleOk}>
              发红包
            </Button>,
          ]}
        >
          <Form labelCol={{ span: 5 }} wrapperCol={{ span: 12 }} >
          <Form.Item label="红包类型">
          {getFieldDecorator('redPacket_type', {
            initialValue: 0,
            rules: [{ required: true, message: '请输入红包金额!'}],
          })(
            <RadioGroup>
            <Radio value={0}>拼手气</Radio>
            <Radio value={1}>平均</Radio>
        </RadioGroup>
          )}
        </Form.Item>
        <Form.Item label="红包金额">
          {getFieldDecorator('total_money', {
            rules: [{ required: true, message: '请输入红包金额!' }],
          })(<Input type="number" />)}
        </Form.Item>
        <Form.Item label="红包个数">
          {getFieldDecorator('redPacket_size', {
            rules: [{ required: true, message: '请输入红包个数!' }],
          })(<Input type="number" placeholder="最大个数为200"/>)}
        </Form.Item>
      </Form>
        </Modal>
         <Divider/>
       <List
         grid={{ gutter: 16, column: 10 }}
         dataSource={redpacketList}
         locale={{emptyText: "暂无红包"}}
         style={{ outline: 'none' }}
         renderItem={item => (
      <List.Item>
        <Card title={item.title}>Card content</Card>
      </List.Item>
    )}
  />
    
    
    
    </div>
    )
 }
}
export default RedPacketSquare