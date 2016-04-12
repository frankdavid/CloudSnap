@main = angular.module('main', [])
@main.controller('MainCtrl', ['$scope', ($scope) ->

  $scope.focus = ->
    if $scope.url == ''
      $scope.url = 'http://'

  $scope.blur = ->
    if $scope.url == 'http://'
      $scope.url = ''

  $scope.onSubmit = ->
    window.location.href = "/snap/" + $scope.url

  $("#screenshot").on('load', ->
    $scope.$apply ->
      $scope.screenshotLoaded = true
      $scope.screenshotError  = false
  )

  $("#screenshot").on('error', ->
    $scope.$apply ->
      $scope.screenshotError = true
    window.setTimeout ->
      $("#screenshot").attr('src', $("#screenshot").attr('src') + "?" + Math.random() * 1000)
    , 1500
  )
])