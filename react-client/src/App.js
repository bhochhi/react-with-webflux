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
      bankAccounts: ["bankAccounts1", "bankAccounts2"],
      insuranceAccounts: ["insuranceAccounts1", "insuranceAccounts2"],
      transactions: ["transaction1", "transaction2"]
    };
  }


  componentDidMount() {

    // const socker$ = ajax('ws://localhost:8080/ws');
    // const insurance$ = ajax('http://localhost:8080/products/insurance');
    // const transactions$ = ajax('http://localhost:8080/transactions');

    const socket = new WebSocket('ws://localhost:8080/ws/allinone');
    // const socket = new WebSocket('ws://localhost:8080/ws/profiles');

    // socket.addEventListener('message', event => { 
    //   console.log(event);
    //   // const bankData = JSON.parse(event.data);
    //   // console.log(bankData);
    //   // this.setState({ bankAccounts: bankData });
    // });

    socket.addEventListener('message', function (event) {
      console.log('message: ',event);
      // socket.send('Thank you for message: '+event.data);
    // window.alert('message from server: ' + event.data);
  });

  socket.addEventListener('open', function (m) { console.log("websocket connection open"); 
      
      socket.send("GET_PRODUCTS")
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
