#!/usr/bin/env bash

export DEFAULT_HOSTNAME=localhost
export DEFAULT_PORT=8080

echo --------
curl -i -X POST http://${DEFAULT_HOSTNAME}:${DEFAULT_PORT}/usertracking/rest/service/contact -H "Content-Type: application/json" -d '
{
	"id": 1,
    "email": "marcus.duty@gmail.com",
    "url": "www.resultadosdigitais.com.br",
    "datetime": "20172710 101010"
}
'
echo
