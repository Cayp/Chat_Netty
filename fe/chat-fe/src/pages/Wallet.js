import React from 'react';
import { Col,Row, Divider, Table, Tag } from 'antd';
import moment from 'moment'
import 'antd/dist/antd.css';
import { myAxios } from '../utils/myAxios';

class Wallet extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            wallet: undefined,
            logs: []
        }
    }

    componentDidMount() {
     this.getWallet()
     this.getWalletLogs()
    }

    getWallet = () => {
      myAxios.get('/chat/redpacket/logs')
             .then((response) => {
                 let json = response.data;
                 if (json.code === 20000) {
                     this.setState({ logs: json.dataList })
                 }
             })
    }

    getWalletLogs = () => {
       myAxios.get('/chat/redpacket/wallet')
              .then((response) => {
                  let json = response.data;
                  if (json.code === 20000) {
                      this.setState({ wallet: json.data })
                  }
              })
    }

    handleType = (type) => {
         if (type === 502) {
             return 'volcano'
         } else if (type === 501) {
             return 'green'
         } else if (type === 500) {
             return 'geekblue'
         } else {
             return 'geekblue'
         }
    }

    handleMessage = (type) => {
        if (type === 502) {
            return '发红包'
        } else if (type === 501) {
            return '抢红包'
        } else if (type === 500) {
            return '红包退还'
        } else {
            return '默认'
        }
   }

   handleMoney = (type, money) => {
    if (type === 502) {
        return `-${money}`
    } else if (type === 501) {
        return `+${money}`
    } else if (type === 500) {
        return `+${money}`
    } else {
        return ''
    }
   }

    render() {
      const { wallet, logs } = this.state;
      const columns = [
          {
              title: '类型',
              dataIndex: 'type',
              key: 'type',
              render: type => (<Tag color={this.handleType(type)} key={type}>
               {this.handleMessage(type)}
              </Tag>),
          },
          {
              title: '时间',
              dataIndex: 'time',
              key: 'time',
              render: time => moment(time * 1000).format('YYYY-MM-DD HH:mm:ss'),
          },
          {
              title: '金额变动',
              dataIndex: 'money',
              key: 'money',
              render: (money, record) => this.handleMoney(record.type, money),
          }
        ]
      return (
          <div style={{ padding: 30}}>
            <Row>
                <Col span={1}>
                 余额:
                </Col>
                <Col span={1}>
                <h2>{wallet ? wallet.money : 0}</h2>
                </Col>
            </Row>
            <Divider/>
            <h1>钱包流水:</h1>
            <Table columns={columns} dataSource={logs} locale={{emptyText: '暂无流水'}}/>
          </div>
      )
    }
}
export default Wallet