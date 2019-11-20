import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';

const container = {
    width: 70,
    margin: 0,
    paddingTop: 40
};

const tableContainer = {
    width: "100%",
    padding: "20px",
    display: "inline-block",
    backgroundColor: "#161e29",
    boxSizing: "border-box",
    margin: 5,
    border: "1px solid #66FCF1"
};

class Databases extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: []
        };
    }

    componentDidMount() {
        fetch("http://localhost:8080/database",
            {
                method: 'get',
                dataType: 'json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            })
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    this.setState({
                        items: result.map(item => item),
                        isLoaded: true,
                        error: null
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const {error, isLoaded, items} = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>
        } else {
            return (
                items.map(
                    item => {
                        console.log(item);
                        return <div><DatabaseContainer styles={container} name={item.databaseName}/> <MergeForm/></div>;
                    }
                )
            );
        }
    }
}

class DatabaseContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            isClicked: false,
            name: props.name
        };
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        fetch("http://localhost:8080/database/" + this.state.name,
            {
                method: 'get',
                dataType: 'json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            })
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    this.setState({
                        database: {
                            databaseName: result.databaseName,
                            tables: result.tables.map(table => ({
                                key: table.key,
                                tableName: table.name,
                                columns: table.columns,
                                rows: table.rows
                            }))
                        },
                        isLoaded: true,
                        error:
                            null,
                        isClicked:
                            true,
                    })
                    ;
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );
        console.log(this.state.name);
    }

    render() {
        console.log(this.state.isClicked);
        return (
            <div>
                <div onClick={this.handleClick.bind(this)}>
                    {this.state.name}
                </div>
                {
                    (this.state.isClicked)
                        ? this.state.database.tables.map(table =>
                            <TableContainer table={table} styles={tableContainer} dbName={this.state.name} key={table.name}/>
                        )
                        : <div>no table</div>
                }
            </div>
        )
            ;
    }
}

class TableContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            table: props.table,
            dbName: props.dbName,
            isClicked: false
        };
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        fetch("http://localhost:8080/database/" + this.state.dbName + "/table/" + this.state.table.tableName,
            {
                method: 'get',
                dataType: 'json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            })
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    this.setState({
                        table: result,
                        isLoaded: true,
                        error: null,
                        isClicked: true
                    });
                },
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            );
        console.log(this.state.table.tableName);
    }


    render() {
        console.log("been here");
        console.log(this.state.table);
        return <div>
            <div onClick={this.handleClick.bind(this)}>
                {this.state.table.tableName}
            </div>
            {
                (this.state.isClicked)
                    ? <Table table={this.state.table} dbName={this.state.dbName}/>
                    : <div></div>
            }
        </div>;
    }
}

class Table extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            table: props.table,
            dbName: props.dbName,
            isClicked: false
        };
        //this.handleClick = this.handleClick.bind(this);
    }

    render() {
        console.log(this.state.table.columns.map(column => (column.columnName.toString() + ": " + column.columnType.toString())).join("\n"));
        return <table>
            <thead>
            <tr>
                {this.state.table.columns.map(column => <th>{(column.columnName.toString() + ": " + column.columnType.toString()).toString()}</th>)}
            </tr>
            </thead>
            <tbody>
            {
                this.state.table.rows.map(row => row.values).map(rowValue => <tr> {rowValue.map(val =>
                    <td>{val}</td>)}</tr>)
            }</tbody>
        </table>;
    }
}

class MergeForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleFirstChange = this.handleFirstChange.bind(this);
        this.handleSecondChange = this.handleSecondChange.bind(this);
        this.handleThirdChange = this.handleThirdChange.bind(this);
        this.handleDbNameChange = this.handleDbNameChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleFirstChange(event) {
        this.setState({firstValue: event.target.value});
    }
    handleSecondChange(event) {
        this.setState({secondValue: event.target.value});
    }
    handleThirdChange(event) {
        this.setState({joinOn: event.target.value});
    }
    handleDbNameChange(event) {
        this.setState({dbName: event.target.value});
    }

    handleSubmit(event) {
        console.log('A dbName was submitted: ' + this.state.dbName);
        console.log('A first table was submitted: ' + this.state.firstValue);
        console.log('A second table was submitted: ' + this.state.secondValue);
        console.log('A join on was submitted: ' + this.state.joinOn);
        let joinOn = this.state.joinOn;
        fetch("http://localhost:8080/database/" + this.state.dbName + "/table/" + this.state.firstValue + "/merge/" + this.state.secondValue+'?joinOn='+this.state.joinOn, {
            method: 'GET',
            mode: "no-cors",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },

        });
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>Database name:<input type="text" value={this.state.dbName} onChange={this.handleDbNameChange} /></label><br/>
                <label>First table name:<input type="text" value={this.state.firstValue} onChange={this.handleFirstChange} /></label><br/>
                <label>Second table name: <input type="text" value={this.state.secondValue} onChange={this.handleSecondChange} /></label><br/>
                <label>Join on: <input type="text" value={this.state.joinOn} onChange={this.handleThirdChange} /></label><br/>

                <input type="submit" value="Merge" />
            </form>
        );
    }
}

ReactDOM.render(<Databases/>, document.getElementById('root'));

serviceWorker.unregister();
