# encoding: utf-8

from ore.yuiwidget.table import BaseDataTableFormatter

from bungeni.ui import container
from bungeni.ui.i18n import _
from zope.i18n import translate
from zope.security import proxy
from zc.resourcelibrary import need
from zc.table import batching

from z3c.pt.texttemplate import ViewTextTemplateFile
from bungeni.ui.utils import url as ui_url

class TableFormatter(batching.Formatter):
    """The out-of-box table formatter does not let us specify a custom
    table css class."""
    
    table_css_class = "listing grid"
    
    def __call__(self):
        return (
            '''
            <div style="width: 100%%">
              <table class="%s"
                     style="width:100%%"
                     name="%s">
                 %s
              </table>
              %s
            </div>''' % (self.table_css_class, self.prefix,
                         self.renderContents(), self.renderExtra())
            )

class ContextDataTableFormatter(BaseDataTableFormatter):
    script = ViewTextTemplateFile("templates/datatable.pt")
    
    data_view ="/jsonlisting"
    prefix = "listing"
    
    def getFields( self ):
        return container.getFields( self.context )

    def getFieldColumns( self ):
        # get config for data table
        column_model = []
        field_model  = []

        for field in self.getFields( ):
            key = field.__name__
            title =translate(_(field.title), context=self.request)
            coldef = {'key': key, 'label': title, 'formatter': self.context.__name__ }
            if column_model == []:
                column_model.append(
                    """{label:"%(label)s", key:"sort_%(key)s", 
                    formatter:"%(formatter)sCustom", sortable:true, 
                    resizeable:true ,
                    children: [ 
	                { key:"%(key)s", sortable:false}]}""" % coldef
                    )
            else:
                column_model.append(
                    """{label:"%(label)s", key:"sort_%(key)s", 
                    sortable:true, resizeable:true,
                    children: [ 
	                {key:"%(key)s", sortable:false}]
                    }""" % coldef
                    )
            field_model.append(
                '{key:"%s"}'%( key )
                )
        return ','.join(column_model), ','.join(field_model)
    
    def getDataTableConfig(self):
        config = {}
        config['columns'], config['fields'] = self.getFieldColumns()
        config['data_url'] = self.getDataSourceURL()
        config['table_id'] = self.prefix
        config['link_url'] = ui_url.absoluteURL(self.context, self.request)
        config['context_name'] = self.context.__name__
        return config
    
    def __call__(self):
        need('yui-datatable')
        need('yui-paginator')
        need('yui-dragdrop')

        return '<div id="%s">\n<table %s>\n%s</table>\n%s</div>' % (
            self.prefix,
            self._getCSSClass('table'),
            self.renderContents(),
            self.script(**self.getDataTableConfig()))

class AjaxContainerListing(container.ContainerListing):
    formatter_factory = ContextDataTableFormatter

    @property
    def formatter( self ):
        context = proxy.removeSecurityProxy( self.context )
        prefix= "container_contents_" + context.__name__
        formatter = self.formatter_factory( context,
                                            self.request,
                                            (),
                                            prefix=prefix,
                                            columns = self.columns )
        formatter.cssClasses['table'] = 'listing'
        formatter.table_id = "datacontents"
        return formatter

    
