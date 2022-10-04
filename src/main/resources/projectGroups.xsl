<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:param name="projectName"/>
    <xsl:template match="/*[name()='Payload']/*[name()='Projects']/*[name()='Project']">
        <xsl:if test="$projectName = @name">
            <html lang="en">
                <head>
                    <META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                    <title>
                        <xsl:value-of select="$projectName"/>
                    </title>
                </head>
                <body>
                    <table border="1">
                        <tr bgcolor="#9acd32">
                            <th>name</th>
                            <th>desc</th>
                            <th>groups</th>
                        </tr>

                        <tr>
                            <td>
                                <xsl:value-of select="@name"/>
                            </td>
                            <td>
                                <xsl:value-of select="*[name()='description']"/>
                            </td>
                            <td>
                                <xsl:for-each select="*[name()='Groups']/*[name()='Group']">
                                    <p>
                                        <xsl:value-of select="@name"/>
                                    </p>
                                </xsl:for-each>
                            </td>
                        </tr>
                    </table>
                </body>
            </html>
        </xsl:if>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>