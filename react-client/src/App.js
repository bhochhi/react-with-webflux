import React, {
  Component
} from 'react';
import logo from './logo.svg';
import './App.css';
import AccountSection from './components/account-section.component';
import ActivitySection from './components/activity-section.component';

import { Observable, Subject, asapScheduler, pipe, of, from, interval, merge, fromEvent, SubscriptionLike, PartialObserver, combineLatest, range } from 'rxjs';
import {
  map,
  filter
} from 'rxjs/operators';
import {
  ajax
} from 'rxjs/ajax';

class App extends Component {
  constructor(props, context) {
    super(props, context);

    this.state = {
      bankAccounts: [{id:'uuid',"name":"dummy name"}],
      insuranceAccounts: [{id:'uuid1',"name":"insurance 1"},{id:'uuid444',"name":"insuranceddd"}],
      transactions: [{id:"trans1",title:"transaction1"}]
    };

  }


  componentDidMount() {

    // const socker$ = ajax('ws://localhost:8080/ws');
    // const insurance$ = ajax('http://localhost:8080/products/insurance');
    // const transactions$ = ajax('http://localhost:8080/transactions');

    const socket = new WebSocket('ws://localhost:8080/ws/allinone');

    let that = this;
    socket.addEventListener('message', function (event) {
      console.log('message: ',event);
      if(event.data === 'PROD-ERROR' || event.data === 'TRANS-ERROR'){
        console.log("Error message: ",event.data);
      }else{
        var jsonData = JSON.parse(event.data);
        console.log("server message: ",jsonData);
        if(jsonData.type ==='bank'){  
          let bankAccounts = that.state.bankAccounts.map(a => ({...a})); //making a deep copy
          let account = bankAccounts.find(ac=>ac.id===jsonData.id);
          if(!!account){
              bankAccounts.forEach(ac=>{
              if(ac.id === jsonData.id){
                ac.name = jsonData.name
              }
            })
          }else{
            bankAccounts.push({id:jsonData.id,name:jsonData.name})
          }
          that.setState({bankAccounts: bankAccounts});
        }else if(jsonData.type ==='pnc'){
          let insuranceAccounts = that.state.insuranceAccounts.map(a => ({...a}));
          let account = insuranceAccounts.find(ac=>ac.id===jsonData.id);
          if(!!account){
            insuranceAccounts.forEach(ac=>{
              if(ac.id === jsonData.id){
                ac.name = jsonData.name
              }
            })
          }else{
            insuranceAccounts.push({id:jsonData.id,name:jsonData.name})
          }
          that.setState({insuranceAccounts: insuranceAccounts});         
        }else{

          let copytransactions = that.state.transactions.map(a => ({...a})); 
          let trans = copytransactions.find(ac=>ac.id===jsonData.id);
          if(!!trans){
            copytransactions.forEach(ac=>{
              if(ac.id === jsonData.id){
                ac.title = jsonData.title
              }
            })
          }else{
            copytransactions.push({id:jsonData.id, title:jsonData.title});
          }
          that.setState({transactions: copytransactions});                    
        }
      }
      // socket.send('Thank you for message: '+event.data);
    // window.alert('message from server: ' + event.data);
  });

 
  socket.addEventListener('open', function (m) { console.log("websocket connection open"); 
      
      //socket.send("GET_PRODUCTS")
      // socket.send("GET_TRANSACTIONS")

});


    // const subscription = combineLatest(banking$, insurance$, transactions$)
    //   .subscribe(([bankRes, insuranceRes,transRes]) => {
    //     let bankAccountData = bankRes.response.map(e => e.name);
    //     let insuranceAccountData = insuranceRes.response.map(e => e.name);
    //     let transactonData = transRes.response.map(e => e.title);

    //     //simulating delay
    //     setTimeout(() => {
    //       this.setState({ 
    //         bankAccounts: bankAccountData
    //       });
    //     }, 4000);
    //     setTimeout(() => this.setState({
    //       insuranceAccounts: insuranceAccountData
    //     }), 3000);
    //     setTimeout(() => this.setState({
    //       transactions: transactonData
    //     }), 1000);

    //   }, e1 => console.error(e1));
      
      

  }

  


  render() {
    return ( 
    <div className = "App" >
      <AccountSection bankAccounts = {
        this.state.bankAccounts
      }
      insuranceAccounts = {
        this.state.insuranceAccounts
      }
      /> 
      <hr />
      <ActivitySection bills = {
        this.state.bills
      }
      transactions = {
        this.state.transactions
      }
      /> 
      </div>
    );
  }
}
export default App;
