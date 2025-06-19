# HUGO 

## Functions

https://gohugo.io/functions/compare/eq/

## Debugging

https://www.andreffs.com/blog/debug-in-hugo/

    <script>
        var hugoLog = JSON.parse({{ jsonify . }});
        console.log('Hugo Debug: ', hugoLog);
    </script>

https://discourse.gohugo.io/t/howto-show-what-values-are-passed-to-a-template/41

    {{ printf "%#v" . }}