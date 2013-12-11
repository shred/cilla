<#assign subject="New Comment">
Hello!

A new comment has been posted!

Article: ${title}
Link:    ${link}

Name:    ${comment.name!"anonymous"}
Email:   ${comment.mail!}
URL:     ${comment.url!}
Text:

---------------------------------------------------------------------
${comment.text.text}
---------------------------------------------------------------------

<#if comment.published>
The comment has been published automatically.
<#else>
The comment has not been published yet. Please log in and moderate the comment.
</#if>

-- 
Cilla
