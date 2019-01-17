import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import AccountSection from './components/account-section.component';
import ActivitySection from './components/activity-section.component';

class App extends Component {
  constructor(props, context) {
    super(props, context);

    this.state = {
        bankAccounts: ["bankAccounts1", "bankAccounts2"],
        insuranceAccounts: ["insuranceAccounts1", "insuranceAccounts2"],
        transactions: ["transaction1","transaction2"]
    };
  }
   
  
  componentDidMount(){
    
    let bank = [
      {
        name:'bankAc1'      
      },
      {
        name:'bankAc2'      
      },
      {
        name:'bankAc3'      
      },
      {
        name:'bankAc4'      
      }
    ]
    let insurance = [
      {
        name:'insurance1'      
      },
      {
        name:'insurance2'      
      },
      {
        name:'insurance3'      
      },
      {
        name:'insurance4'      
      }
    ]

    let trans = [
      {
        name:'trans1 desc'      
      },
      {
        name:'trans2 desc'      
      },
      {
        name:'trans3 desc'      
      },
      {
        name:'trans4 desc'      
      }
    ]



    const banking$ = ajax({
      url: 'http://localhost:8080/banking',
      method: 'POST',
      mode:'cors',
      body: JSON.stringify(insurance), 
      headers:{
        'Content-Type': 'application/json'
      }
    })
    .then(res => new Promise(resolve => setTimeout(() => resolve(res.json()), 1000)))
    .then(respone=>{
      let accountData = respone.json.map(e=>e.name)
      console.log('accountData',accountData);
      this.setState({insuranceAccounts:accountData});
    })
    .catch(err=> console.log(err));

    fetch("https://httpbin.org/anything/banking", {
      method: 'POST',
      mode:'cors',
      body: JSON.stringify(bank), 
      headers:{
        'Content-Type': 'application/json'
      }
    })
    .then(res => new Promise(resolve => setTimeout(() => resolve(res.json()), 3000)))
    .then(respone=>{
      let accountData = respone.json.map(e=>e.name)
      console.log('accountData',accountData);
      this.setState({bankAccounts:accountData});
    })
    .catch(err=> console.log(err));

    fetch("https://httpbin.org/anything/transactions", {
      method: 'POST',
      mode:'cors',
      body: JSON.stringify(trans), 
      headers:{
        'Content-Type': 'application/json'
      }
    })
    .then(res => new Promise(resolve => setTimeout(() => resolve(res.json()), 4000)))
    .then(respone=>{
      let accountData = respone.json.map(e=>e.name)
      console.log('accountData',accountData);
      this.setState({transactions:accountData});
    })
    .catch(err=> console.log(err));

  }


  render() {
    return (
      <div className="App">
        <AccountSection bankAccounts={this.state.bankAccounts} insuranceAccounts={this.state.insuranceAccounts} />
        <ActivitySection bills={this.state.bills} transactions={this.state.transactions} />
      </div>
    );
  }
}

export default App;
