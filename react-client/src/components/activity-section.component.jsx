
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
            console.log(ac)
            return (<li key={Math.random()} >{ac}</li>)
        });

        return (
            <div className="activity-section">
                <h1>Recent Transactions</h1>
                {transactions}
            </div>
        );
    }
}

export default ActivitySection;