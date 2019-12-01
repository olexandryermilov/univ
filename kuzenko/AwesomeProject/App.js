import React, {Component} from 'react';
import {Platform, StyleSheet} from 'react-native';
import {Text, TextInput, View, Button} from 'react-native';
import {Table, TableWrapper, Row, Rows, Col, Cols, Cell} from 'react-native-table-component';


const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
    android: 'Double tap R on your keyboard to reload,\n' + 'Shake or press menu button for dev menu',
});

import ReactDOM from 'react-dom';
//import './index.css';
//import * as serviceWorker from './serviceWorker';

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
        fetch("http://35.193.110.99:8080/database",
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
            return <Text>Error: {error.message}</Text>;
        } else if (!isLoaded) {
            return <Text>Loading...</Text>
        } else {
            return (
                items.map(
                    item => {
                        console.log(item);
                        return <View><DatabaseContainer styles={container} name={item.databaseName}/></View>;
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
        fetch("http://35.193.110.99:8080/database/" + this.state.name,
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
                                tableName: table.tableName,
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
        var thisdbName = this.state.name;
        return (
            <View>
                <Text onPress={this.handleClick.bind(this)}>
                    {this.state.name}
                </Text>
                {
                    (this.state.isClicked)
                        ? this.state.database.tables.map(table =>
                            <TableContainer table={table} style={styles.tableContainer} dbName={thisdbName}/>
                        )
                        : <Text>no table</Text>
                }
            </View>
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
        fetch("http://35.193.110.99:8080/database/" + this.state.dbName + "/table/" + this.state.table.tableName,
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
        console.log("hello world");
    }


    render() {
        console.log("been here");
        console.log(this.state.table);
        return <View>
            <Text onPress={this.handleClick.bind(this)}>
                {this.state.table.tableName}
            </Text>
            {
                (this.state.isClicked)
                    ? <DbTable table={this.state.table} dbName={this.state.dbName}/>
                    : <Text></Text>
            }
        </View>;
    }
}

class DbTable extends React.Component {
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
        let headRow = this.state.table.columns.map(col => col.columnName.toString() +": " + col.columnType.toString());
        let otherRows = this.state.table.rows.map(row => row.values).map(rowValue =>
            rowValue.map(val =>val.toString())
        );
        console.log("@@@@@@");
        console.log(headRow);
        console.log("@@@!!!");
        console.log(otherRows);
        return (<View>
            <Table>
                <Row data = {headRow}>
                </Row>
                <Rows data={otherRows}/>
            </Table>
        </View>);
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

    handleFirstChange(text) {
        this.setState({firstValue: text});
    }

    handleSecondChange(text) {
        this.setState({secondValue:text});
    }

    handleThirdChange(text) {
        this.setState({joinOn: text});
    }

    handleDbNameChange(text) {
        this.setState({dbName: text});
    }

    handleSubmit() {
        let dbName = this.state.dbName;
        let firstValue = this.state.firstValue;
        let secondValue = this.state.secondValue;
        let joinOn = this.state.joinOn;
        console.log('A dbName was submitted: ' + dbName);
        console.log('A first table was submitted: ' + firstValue);
        console.log('A second table was submitted: ' + secondValue);
        console.log('A join on was submitted: ' + joinOn);
        console.log("http://35.193.110.99:8080/database/" + dbName + "/table/" + firstValue + "/merge/" + secondValue + '?joinOn=' + joinOn);
        fetch("http://35.193.110.99:8080/database/" + dbName + "/table/" + firstValue + "/merge/" + secondValue + '?joinOn=' + joinOn, {
            method: 'GET',
            mode: "no-cors",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },

        }).then(res => res.json())
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
            );;
    }

    render() {
        return (
            <View>
                <TextInput
                    style={{height: 40}}
                    placeholder="Database name"
                    onChangeText={(text) => this.handleDbNameChange(text)}
                    value={this.state.text}
                />
                <TextInput
                    style={{height: 40}}
                    placeholder="First table name:"
                    onChangeText={(text) => this.handleFirstChange(text)}
                    value={this.state.text}
                />
                <TextInput
                    style={{height: 40}}
                    placeholder="Second table name:"
                    onChangeText={(text) => this.handleSecondChange(text)}
                    value={this.state.text}
                />
                <TextInput
                    style={{height: 40}}
                    placeholder="Join on: "
                    onChangeText={(text) => this.handleThirdChange(text)}
                    value={this.state.text}
                />
                <Button
                    onPress={() => {
                        alert('You tapped the button!');
                        this.handleSubmit()
                    }}
                    title="Press Me"
                />
            </View>
        );
    }
}

export default class App extends Component {
    render() {
        return (
            <View>
                <Text>Hello, Olexandr!</Text>
                <Databases style={styles.container}>
                </Databases><MergeForm/></View>
        );
    }
}


const styles = StyleSheet.create({
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
    tableContainer: {
        width: "100%",
        padding: "20px",
        //display: "inline-block",
        backgroundColor: "#161e29",
        //boxSizing: "border-box",
        margin: 5,
        //border: "1px solid #66FCF1"
    },
    container: {
        width: 70,
        margin: 0,
        paddingTop: 40
    }
});
