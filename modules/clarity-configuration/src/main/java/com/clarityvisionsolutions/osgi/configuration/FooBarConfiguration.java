package com.clarityvisionsolutions.osgi.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

@ExtendedObjectClassDefinition(
    category = "foo-bar",
    scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
    id = "com.clarityvisionsolutions.osgi.configuration.FooBarConfiguration",
    localization = "content/Language",
    name = "foo-bar-configuration-name"
)
public interface FooBarConfiguration {

    @Meta.AD(
        deflt = "",
        description = "foo-bar-activation-key-description",
        name = "foo-bar-activation-key-name",
        required = false
    )
    public String activationKey();

    @Meta.AD(
        deflt = "false",
        description = "foo-bar-enabled-description",
        name = "foo-bar-enabled-name",
        required = false
    )
    public boolean enabled();

    @Meta.AD(
        deflt = "https://api.foobar.example.com/v1",
        description = "foo-bar-service-url-description",
        name = "foo-bar-service-url-name",
        required = false
    )
    public String serviceUrl();

}
