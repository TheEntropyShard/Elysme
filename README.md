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
- Download the `deltachat-rpc-server` executable from [chatmail/core](https://github.com/chatmail/core) repository and provide absolute path to it
via the `RPC_SERVER_PATH` environment variable to Elysme.
- For now, the only way to add an account is by importing a backup from another Delta Chat client.