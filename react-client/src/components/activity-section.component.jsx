
import React, { Component } from 'react';

class ActivitySection extends Component {

    constructor(props, context) {
        super(props, context);
        this.state = {
            transactions: this.props.transactions
        };

    }

    
    componentWillReceiveProps(nextProps) {
		this.setState({ transactions: nextProps.transactions });  
    }

    render() {
        var transactions = this.state.transactions.map((ac) => {
            return (<li key={ac.id} >{ac.title}</li>)
        });

        return (
            <div className="activity-section">
                <h1 className="trans-title">Recent Transactions</h1>
                
                {transactions}
            </div>
        );
    }
}

export default ActivitySection;