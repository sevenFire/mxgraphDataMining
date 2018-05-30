<%
    String projectPath = request.getContextPath();
%>

<script type="text/javascript">
    var projectPath = "<%=projectPath%>";
    var AjaxCommunicator = {
        ajaxRequest: function (serviceUrl, methodWay, paramJsonObj, callBackFunct,
                               ajaxMode) { // 发送ajax请求
            if (ajaxMode != true)
                ajaxMode = false;
            $.ajax({
                       url: projectPath + serviceUrl,
                       method: methodWay,
                       async: ajaxMode,
                       data: {
                           'ajaxParam': JSON.stringify(paramJsonObj)
                       },
                       dataType: 'json',
                       success: function (jsonData) {
                           if (callBackFunct) {
                               if (callBackFunct.onSuccess) {
                                   callBackFunct.onSuccess(jsonData);
                               }
                           }
                       },
                       error: function (xmlR, status, e) {
                           console.log(e);
                           if (callBackFunct) {
                               if (callBackFunct.onError) {
                                   callBackFunct.onError(xmlR, status, e);
                               }
                           }
                       }
                   });
        }
    };
</script>

