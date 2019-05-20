@echo off
ssh -o ServerAliveInterval=60 -R postter:80:localhost:8080 serveo.net