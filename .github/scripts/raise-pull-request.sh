#!/usr/bin/env bash

language=$1

generator_repo_name=$(jq --raw-output .repository.name "$GITHUB_EVENT_PATH")

version=$(grep -Po '"packageVersion":.*?[^\\]",' generator/languages/$language/openapi-generator-config.json | cut -c20-24)
if [ -z "$version" ]
then
  version=$(grep -Po '"artifactVersion":.*?[^\\]",' generator/languages/$language/openapi-generator-config.json | cut -c21-25)
fi
pr_number=$(jq --raw-output .pull_request.number "$GITHUB_EVENT_PATH")

cd sdk

git config user.email $USER_EMAIL
git config user.name $USER
git config --global core.autocrlf true

branch_name="feat/sdk/$generator_repo_name-pr-$pr_number"
remote_branch_check=$(git ls-remote --heads origin $branch_name)

if [ -z "$remote_branch_check" ]
then
  echo "Switching to new branch '$branch_name'"
  git checkout -b $branch_name
  rm -r auto-generated-sdk
  cp -r ../generator/languages/$language/sdk auto-generated-sdk
  git add -A .
  if [[ `git status --porcelain` ]] 
  then 
    git commit -m "feat(sdk): Auto-commit from '$generator_repo_name' repository PR $pr_number for SDK version v$version"
    echo "Committed all the changes"
    
    git push origin $branch_name
    echo "Pushed to remote location"
    
    export GITHUB_USER=$USER
    export GITHUB_TOKEN=$USER_API_KEY
    hub pull-request -m "feat(sdk): Auto-created from '$generator_repo_name' repository PR $pr_number for SDK version v$version" -h $branch_name
  else
    echo "No changes to commit" 
  fi
else
  echo "Switching to existing branch '$branch_name'"
  git checkout $branch_name
  git pull
  rm -r auto-generated-sdk
  cp -r ../generator/languages/$language/sdk auto-generated-sdk
  git status
  if git diff-index --quiet HEAD -- 
  then 
    echo "No changes to commit" 
  else 
    git add -A .
    git commit -m "feat(sdk): Auto-commit from '$generator_repo_name' repository PR $pr_number for SDK version v$version"
    echo "Committed all the changes"
  
    git push origin $branch_name
    echo "Pushed to remote location"
  fi
fi
