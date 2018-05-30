Resultbar = function(editorUi, container)
{
    this.editorUi = editorUi;
    this.container = container;
};

/**
 * Returns information about the current selection.
 */
Resultbar.prototype.labelIndex = 0;
Resultbar.prototype.showCloseButton = true;

/**
 * Background color for inactive tabs.
 */
Resultbar.prototype.inactiveTabBackgroundColor = '#d7d7d7';

/**
 * Adds the label menu items to the given menu and parent.
 */
Resultbar.prototype.init = function()
{
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    this.update = mxUtils.bind(this, function(sender, evt)
    {
        this.clearSelectionState();
        this.refresh();
    });

    //图形改变，开始，结束的时候刷新
    graph.getSelectionModel().addListener(mxEvent.CHANGE, this.update);
    graph.addListener(mxEvent.EDITING_STARTED, this.update);
    graph.addListener(mxEvent.EDITING_STOPPED, this.update);
    graph.getModel().addListener(mxEvent.CHANGE, this.update);
    graph.addListener(mxEvent.ROOT, mxUtils.bind(this, function()
    {
        this.refresh();
    }));

    this.refresh();
};

/**
 * Returns information about the current selection.
 */
Resultbar.prototype.clearSelectionState = function()
{
    this.selectionState = null;
};


/**
 * Adds the label menu items to the given menu and parent.
 */
Resultbar.prototype.clear = function()
{
    this.container.innerHTML = '';

    // Destroy existing panels
    if (this.panels != null)
    {
        for (var i = 0; i < this.panels.length; i++)
        {
            this.panels[i].destroy();
        }
    }

    this.panels = [];
};

/**
 * Adds the label menu items to the given menu and parent.
 */
Resultbar.prototype.refresh = function()
{
    // Performance tweak: No refresh needed if not visible
    if (this.container.style.height == '0px')
    {
        return;
    }

    this.clear();
    var ui = this.editorUi;
    var graph = ui.editor.graph;

    var div = document.createElement('div');
    div.className='tabRow';

    var label = document.createElement('div');
    label.className='tabLabel';

    this.container.appendChild(div);

    var currentLabel = null;
    var currentPanel = null;
    var currentMode_Is_Job=ui.currentMode_Is_Job;

    var addClickHandler = mxUtils.bind(this, function(elt, panel, index)
    {
        var clickHandler = mxUtils.bind(this, function(evt)
        {
            if (currentLabel != elt)
            {
                this.labelIndex = index;

                if (currentLabel != null)
                {
                    currentLabel.style.backgroundColor = this.inactiveTabBackgroundColor;
                    currentLabel.style.borderBottomWidth = '1px';
                }

                currentLabel = elt;
                currentLabel.style.backgroundColor = '#ffffff';
                currentLabel.style.borderBottomWidth = '0px';

                if (currentPanel != panel)
                {
                    if (currentPanel != null)
                    {
                        currentPanel.style.display = 'none';
                    }

                    currentPanel = panel;
                    currentPanel.style.display = '';
                }
            }
        });

        mxEvent.addListener(elt, 'click', clickHandler);

        if (index == this.labelIndex)
        {
            // Invokes handler directly as a workaround for no click on DIV in KHTML.
            clickHandler();
        }
    });

    label.style.backgroundColor = this.inactiveTabBackgroundColor;
    //label.style.width = (currentMode_Is_Job) ? '50%' : '50%';
    label.style.width='100px';
    var label2 = label.cloneNode(false);

    // Workaround for ignored background in IE
    label2.style.backgroundColor = this.inactiveTabBackgroundColor;

    // Log
    mxUtils.write(label, mxResources.get('log'));
    div.appendChild(label);

    var logPanel = document.createElement('div');
    logPanel.className='logPanel';
    logPanel.style.display = 'none';

    this.panels.push(new LogPanel(this, ui, logPanel));
    this.container.appendChild(logPanel);
    addClickHandler(label, logPanel, 0);


    if (currentMode_Is_Job)
    {
        // JobMeasure
        mxUtils.write(label2, mxResources.get('jobMeasure'));
        div.appendChild(label2);

        var jobMeasurePanel = document.createElement('div');
        jobMeasurePanel.className='jobMeasurePanel';
        jobMeasurePanel.style.display = 'none';
        this.panels.push(new JobMeasurePanel(this, ui, jobMeasurePanel));
        this.container.appendChild(jobMeasurePanel);

        addClickHandler(label2, jobMeasurePanel, 1);
    }
    else
    {
        // StepMeasure
        mxUtils.write(label2, mxResources.get('stepMeasure'));
        div.appendChild(label2);

        var stepMeasurePanel = document.createElement('div');
        stepMeasurePanel.style.display = 'none';
        stepMeasurePanel.className='stepMeasurePanel';
        this.panels.push(new StepMeasurePanel(this, ui, stepMeasurePanel));
        this.container.appendChild(stepMeasurePanel);

        addClickHandler(label2, stepMeasurePanel, 1);
    }
};

/**
 * Base class for format panels.
 */
BaseResultPanel = function(resultbar, editorUi, container)
{
    this.resultbar = resultbar;
    this.editorUi = editorUi;
    this.container = container;
    this.listeners = [];
};

/**
 *
 */
BaseResultPanel.prototype.buttonBackgroundColor = 'white';

/**
 * Adds the given option.
 */
BaseResultPanel.prototype.createPanel = function()
{
    var div = document.createElement('div');

    return div;
};


/**
 *
 */
BaseResultPanel.prototype.addKeyHandler = function(input, listener)
{
    mxEvent.addListener(input, 'keydown', mxUtils.bind(this, function(e)
    {
        if (e.keyCode == 13)
        {
            this.editorUi.editor.graph.container.focus();
            mxEvent.consume(e);
        }
        else if (e.keyCode == 27)
        {
            if (listener != null)
            {
                listener(null, null, true);
            }

            this.editorUi.editor.graph.container.focus();
            mxEvent.consume(e);
        }
    }));
};


/**
 * Adds the label menu items to the given menu and parent.
 */
BaseResultPanel.prototype.destroy = function()
{
    if (this.listeners != null)
    {
        for (var i = 0; i < this.listeners.length; i++)
        {
            this.listeners[i].destroy();
        }

        this.listeners = null;
    }
};





