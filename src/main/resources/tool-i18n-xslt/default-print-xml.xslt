<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:strip-space elements="*" />

	<xsl:output method="xml" encoding="UTF-8" />

	<xsl:template match="@*|node()">
		<xsl:choose>
			<xsl:when test="text()">
			    <xsl:copy>
			      <xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
			      <xsl:value-of disable-output-escaping="yes" select="text()"/>
			      <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			      </xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy>
					<xsl:apply-templates select="@*|node()" />
				</xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>