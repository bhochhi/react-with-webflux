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
      bankAccounts: [{id:'uuid',"name":"dummy name"},{id:'uuid-need-to-be-removed',name:"my old nickname"}],
      insuranceAccounts: [{id:'uuid1',"name":"insurance 1"},{id:'uuid444',"name":"insuranceddd"}],
      transactions: [{id:"trans1",title:"transaction1"}]
    };
    this.processMessage = this.processMessage.bind(this);
    this.processBankProduct = this.processBankProduct.bind(this);
    this.removeProduct = this.removeProduct.bind(this);
  }

  processMessage(event){
      console.log('message: ',event);
        var jsonData = JSON.parse(event.data);
       if(jsonData.type ==='pnc'){
          let insuranceAccounts = this.state.insuranceAccounts.map(a => ({...a}));
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
          this.setState({insuranceAccounts: insuranceAccounts});         
        }else{

          let copytransactions = this.state.transactions.map(a => ({...a})); 
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
          this.setState({transactions: copytransactions});                    
        }
  }

  removeProduct(event){
    console.log('message to remove project: ',event);
        var jsonData = JSON.parse(event.data);
        let bankAccounts = this.state.bankAccounts.filter(ac=>ac.id !== jsonData.id);
        this.setState({bankAccounts: bankAccounts});
  }

  processBankProduct(event){
    console.log('bankProduct event: ',event);
      var jsonData = JSON.parse(event.data);
        let bankAccounts = this.state.bankAccounts.map(a => ({...a})); //making a deep copy
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
        this.setState({bankAccounts: bankAccounts});
}

componentDidMount() {

//REST
    // const insurance$ = ajax('http://localhost:8080/products/insurance');
    // const transactions$ = ajax('http://localhost:8080/transactions');


//WebSocket
    // const socket = new WebSocket('ws://localhost:8080/ws/allinone'); 
    // socket.addEventListener('message', this.processMessage);


//SSE

    if (typeof(EventSource) === undefined) {
        alert('Error: Server-Sent Events are not supported in your browser');
        return;
    }

    const eventSource = new EventSource('http://localhost:8080/products/sse'); 

    eventSource.addEventListener('bankProduct', this.processBankProduct);
    eventSource.addEventListener('removeProduct', this.removeProduct);

    eventSource.addEventListener('disconnect',  function(event){
      console.log('disconnect event: all products received',event);
      eventSource.close();
    });

    eventSource.onmessage =  this.processMessage
    eventSource.onopen = event => console.log('sse open', event); 

    eventSource.onerror = event => {
      console.log('sse error', event);
      eventSource.close();
      if(event.readyState === EventSource.CLOSED){
          console.log('see connection closed',eventSource);
          eventSource.close();
      }
    };

  }

  


  render() {
    return ( 
    <div className = "App App-header" >
      <AccountSection bankAccounts = {
        this.state.bankAccounts
      }
      insuranceAccounts = {
        this.state.insuranceAccounts
      }
      /> 
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
