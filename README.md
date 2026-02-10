# Elysme

> [!WARNING]  
> This project is not yet ready for daily use. Do not have any expectations.

Client for [DeltaChat](https://delta.chat/)

## Quick Start
To build and run development version, execute:
```shell
gradlew run
```

## How to Use
To be able to use the project in its current state, before running, you need to set two environment variables:
- `RPC_SERVER_PATH` - An absolute path to the `deltachat-rpc-server` executable from [chatmail/core](https://github.com/chatmail/core) repository
- `DC_ACCOUNTS_PATH` - An absolute path to the folder `accounts` created by another DeltaChat client, as for now there is no way to start a conversation from Elysme

Also make sure that no other DeltaChat client is operating on the same `accounts` folder at the moment, otherwise Elysme will silently fail to show GUI but a client process will be started anyway.