/**
 * 生成代码
 * @param id
 */
function generatedCode(id) {
    var disIputs = $('input[disabled=disabled]');
    $(disIputs).removeAttr("disabled");
    var parent = $('#DataFrom').serializeJson();
    if(typeof(id) == 'undefined' || id == null){
        var id = $('#entityId').val();
    }
    var url = "/databasetable/generate/";
    url = url + id;
    var completeHandler = function (resp) {
        alert(resp.data);
    };
    doPost(url,{},completeHandler)
}