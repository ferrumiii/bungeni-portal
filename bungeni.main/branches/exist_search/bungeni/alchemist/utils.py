# Bungeni Parliamentary Information System - http://www.bungeni.org/
# Copyright (C) 2010 - Africa i-Parliaments - http://www.parliaments.info/
# Licensed under GNU GPL v2 - http://www.gnu.org/licenses/gpl-2.0.txt

"""Bungeni alchemist utilities.
$Id$
"""
log = __import__("logging").getLogger("bungeni.alchemist")
import inspect
from sqlalchemy.orm import class_mapper
from bungeni.alchemist.interfaces import IManagedContainer


# type_info - conveniences on capi.get_type_info
# - discriminator may be as specified in: core.type_info._get
# - raises KeyError if not matched

def get_descriptor(discriminator):
    from bungeni.capi import capi
    return capi.get_type_info(discriminator).descriptor_model

def get_interface(discriminator):
    from bungeni.capi import capi
    return capi.get_type_info(discriminator).interface

def get_derived_table_schema(discriminator):
    from bungeni.capi import capi
    return capi.get_type_info(discriminator).derived_table_schema

def get_workflow(discriminator):
    from bungeni.capi import capi
    return capi.get_type_info(discriminator).workflow


# sqlalchemy 

def get_local_table(kls):
    """Get the Selectable which this kls's Mapper manages. May be None.
    
    Note difference to "mapped_table" that is Selectable to which this kls's 
    Mapper is mapped (and in addition to a Table and Alias may slo be a Join.
    """
    return class_mapper(kls).local_table


# misc

def inisetattr(obj, name, value):
    """A once-only setattr (ensure any subsequent attempts to set are to 
    the same value."""
    if getattr(obj, name, None) is not None:
        assert getattr(obj, name) is value
    else:
        setattr(obj, name, value)

def get_managed_containers(context):
    """Get the managed container instances off context
    """
    attrs = []
    kls = context.__class__
    seen = []
    for class_item in inspect.getmro(kls):
        for key, value in class_item.__dict__.items():
            if IManagedContainer.providedBy(value):
                if key not in seen:
                    attrs.append((key, getattr(context, key)))
                    seen.append(key)
    return attrs