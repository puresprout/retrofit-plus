# retrofit-plus

## function
ErrorPolicyCallback - retrofit common error handling for restful API

<img src="https://github.com/puresprout/retrofit-plus/raw/master/images/mock_api_request.png" width="350px">

```
api = retrofit.create(GreetingApi.class);
api.getGreeting().enqueue(new ErrorPolicyCallback<GreetingResponse>(
        ErrorPolicy.createDialog(MainActivity.this),
        ErrorPolicy.createDialog(MainActivity.this)
) {
    @Override
    public void onSuccessfulStatus(Call<GreetingResponse> call, GreetingResponse response) {
        String greeting = response.greeting + " " + response.name + " at " + new Date();

        textView.setText(greeting);
    }
});
```

## ViewType of ErrorPolicy
```
public enum ViewType {
    TOAST,
    SNACKBAR,
    DIALOG,
    INVIEW
}
```

## Gradle
```
compile 'com.purestation:retrofit-plus:0.0.1'
```
