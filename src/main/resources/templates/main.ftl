<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout />
    <span><a href="/user">User list</a></span>
</div>

<div>
    <form method="post" enctype="multipart/form-data">
        <input type="text" name="text" placeholder="Введите сообщение">
        <input type="text" name="tag" placeholder="Тэг">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Добавить</button>
    </form>
</div>

<br/>

<table border="1" align="center" style="text-align:center">
    <tr>
        <td width="50">ID</td>
        <td width="300">TEXT</td>
        <td width="300">TAG</td>
        <td width="100">AUTHOR</td>
        <td width="100">FILE</td>
    </tr>
    <tr>
        <td></td>
        <td>
            <form method="get" action="/main">
                <input type="text" name="filterText" value="${filterText?ifExists}">
                <button type="submit">Найти</button>
        </td>
        <td>
            <input type="text" name="filterTag" value="${filterTag?ifExists}">
            <button type="submit">Найти</button>
            </form>
        </td>
        <td></td>
        <td></td>
    </tr>
    <#list messages as message>
        <tr>
            <td>${message.id}</td>
            <td>${message.text!}</td>
            <td>${message.tag!}</td>
            <td>${message.authorName}</td>
            <td>
                <#if message.filename??>
                    <img src="/img/${message.filename}">
                </#if>
            </td>
        </tr>
    </#list>
</table>

</@c.page>