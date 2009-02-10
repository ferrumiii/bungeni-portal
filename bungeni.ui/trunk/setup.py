from setuptools import setup, find_packages

setup(
    name="bungeni.ui",
    version="0.2",
    author='Bungeni Developers',
    author_email='bungeni-dev@googlegroups.com',
    description='UI for Bungeni, based on Plone',
    license="GPL",
    keywords = "zope3 bungeni",
      classifiers = [
          'Environment :: Web Environment',
          'Intended Audience :: Developers',
          'Programming Language :: Python',
          'Natural Language :: English',
          'Operating System :: OS Independent',
          'Topic :: Internet :: WWW/HTTP',
          'Framework :: Zope3'],    
    packages=find_packages(),
    package_data = { '': ['*.txt', '*.zcml', '*.pt'] },
    namespace_packages=['bungeni'],
    install_requires = [ 'setuptools',
                         'bungeni.core',
                         'bungeni.models',
                         'simplejson',
                         'zope.app.cache',
                         'plone.memoize',
                         'alchemist.ui',
                         'marginalia',
                         'ore.yui',
                         'z3c.menu.ready2go',			
                         'zc.displayname',
                         'z3c.schemadiff',
                         'zope.formlib'],
    zip_safe = False,
    )

