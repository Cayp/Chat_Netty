import React from 'react';
import { List, Card, Divider, Button, Modal, Form, Input, Radio, message,Avatar} from 'antd';
import qs from 'qs'
import { myAxios } from '../utils/myAxios';
import Meta from 'antd/lib/card/Meta';
import { connect, } from 'react-redux'
import { bindActionCreators } from 'redux'
import LoadableComponent from '../utils/LoadableComponent'
import moment from 'moment'

const RadioGroup = Radio.Group;
const form = Form.create({})
const RedpacketDetailModal = LoadableComponent(import('./RedpacketDetailModal'))
const store = connect(
  (state) => ({user: state.user}),
  (dispatch) => bindActionCreators({  }, dispatch)
)


@form@store
class RedPacketSquare extends React.Component {
    constructor(props) {
        super(props);  
        this.state = {
            redpacketList: [],
            visible: false,
            grapsList: [],
            selectItem: {},
            detailVisible: false,
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

getRedpacketGraps = (redpacketId) => {
  myAxios.get(`/chat/redpacket/graps?redpacketId=${redpacketId}`)
         .then((response) => {
             let json = response.data;
             if (json.code === 20000) {
                 this.setState({grapsList: json.dataList})
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

  handleDetailBack = (detailVisible) => {
      this.setState({detailVisible: detailVisible})
  }

  handleCancel = () => {
    this.props.form.resetFields();
    this.setState({
      visible: false,
    });
  };

  handleDetail = (item) => {
    this.getRedpacketGraps(item.redPacketId)
    this.setState({selectItem: item, detailVisible: true})
  }

  grapRedpacket = (redpacketId) => {
    myAxios.post('/chat/redpacket/grap', qs.stringify({redPacketId: redpacketId}))
           .then((response) => {
             let json = response.data;
             if (json.code === 20000) {
              Modal.success({
                content: `抢到${json.data.money}元~`,
                okText: '知道了'
              });
             } else {
              Modal.info({               
                content: json.message,
                okText: '知道了'
              });
             }
           })
           
  }
render() {
    const { redpacketList, visible, confirmLoading, detailVisible, grapsList, selectItem} = this.state
    const { getFieldDecorator } = this.props.form;
    const user = this.props.user
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
            initialValue: 1,
            rules: [{ required: true, message: '请输入红包金额!'}],
          })(
            <RadioGroup>
            <Radio value={1}>拼手气</Radio>
            <Radio value={0}>平均</Radio>
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
         <h1>红包列表:</h1>
         <br/>
       <List
         grid={{ gutter: 10, column: 5}}
         dataSource={redpacketList}
         locale={{emptyText: "暂无红包"}}
         style={{ outline: 'none' }}
         renderItem={item => (
      <List.Item>
        <Card 
        hoverable={true}
        cover={
          <img
            alt="example"
            src={item.redPacket_type === 1 ? require('./imgs/pinshouqi.png') :require("./imgs/putong.jpg")}
          />
        }
        actions={[

          <Button type="danger" onClick={() => this.grapRedpacket(item.redPacketId)}>抢</Button>,
          <Button type="primary" onClick={() => this.handleDetail(item)}>查看</Button>
        ]
        }
        >
        <Meta
         avatar={<Avatar src={`/chat/images/avatar/${item.avatar}`} />}
         title={item.userName}
         description={moment(item.publish_time * 1000).format('YYYY-MM-DD HH:mm:ss')}
        />
        </Card>
        
      </List.Item>
    )}
  />
    <RedpacketDetailModal handleDetailBack={this.handleDetailBack} grapsList={grapsList} detail={selectItem} userId={user.id} visible={detailVisible}/>
    </div>
    )
 }
}
export default RedPacketSquare