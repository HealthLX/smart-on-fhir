#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
    openssl aes-256-cbc -K $encrypted_eb640d164b64_key -iv $encrypted_eb640d164b64_iv -in secret.gpg.enc -out ~\/secret.gpg -d
fi