
## Sections

# Plotting Notes
# - Color Combinations
# - ggplot Notes
# - ggplot Useful Aesthetics, Geoms, and Scales
# - Stock ggplot Lineplot Example
# - Change in Market Value Barplot Example

# ggplot2: Elegant Graphics Notes

# Table Notes
# - Kable Table Notes
# - Stargazer and tex2pdf Notes

#===============================================================================
# Plotting Notes
#===============================================================================

#-------------------------------------------------------------------------------
# Color Combinations
#-------------------------------------------------------------------------------

# Pairs:
# dodgerblue3, firebrick3
# skyblue1, royalblue
# firebrick, darkorange2
# royalblue2, goldenrod1
# cornflowerblue, darkorange2

#-------------------------------------------------------------------------------
# ggplot Notes
#-------------------------------------------------------------------------------

# Grammar of Graphics: You can build any graph from a data set, a coordinate 
# system, and a geom - visual marks that represent data points.

# To display values, map variables in the data to visual properties of the 
# geom (aesthetics) like size, color, and x and y locations.

# --- Common Aesthetics --------------------------------------------------------

color = "royalblue"
fill = GroupName
linetype = "dashed"
#  0 = "blank"
#  1 = "solid"
#  2 = "dashed"
#  3 = "dotted"
#  4 = "dotdash"
#  5 = "longdash"
#  6 = "twodash"
alpha = 0.5 # Opacity of the object.
size = 4
weight = Population

# --- Common Geoms -------------------------------------------------------------

# Line segments.
geom_abline(aes(intercept = 1, slope = 1)) # Line.
geom_hline(aes(yintercept = 1)) # Horizontal line.
geom_vline(aes(xintercept = 1)) # Vertical line.

# One discrete, one continuous variable.
geom_bar() # Bar chart.

# Two continuous variables.
geom_text(aes(label = City)) # Writes text labels on plot.
geom_label(aes(label = City)) # Same as geom_text but draws a rectangle around the text.
geom_point() # Scatterplot.
geom_smooth(method = lm) # Linear regression prediction with confidence interval. 

# Continuous.
geom_line() # Lineplot.
geom_area() # Lineplot that fills in the area under the line.

# Alternative.
library(ggrepel)
geom_text_repel(aes(label = Ticker, color = Size)) # geom_point + geom_text variant that fixes overlapping labels.

# --- Scales -------------------------------------------------------------------

# Scales map data values to the visual values of an aesthetic. 

# General scales example:
scale_fill_manual( # `fill` represents the scale to adjust, this could be `color`, `label`, or another aes.
    values = c("skyblue", "royalblue", "blue", "navy"), # Scale specific arguments, i.e. the colors to use.
    limits = c("d", "e", "p", "r"), # Range of values to include in the mapping.
    breaks = c("d", "e", "p", "r"), # Breaks to use in the legend/axis.
    name = "Fuel Type", # Title to use in the legend/axis.
    labels = "D", "E", "P", "R" # Labels to use in the legend/axis.
)

# General purpose scales:
# Note* that `color` can be changed to any other aes type such as `fill`, `label`, `x` or `y`.
scale_color_continuous() # Automatically map continous values to visual values.
scale_color_discrete() # Automatically map discrete values to visual values. 
scale_color_manual(values = c()) # Manually map discrete values to visual values.
scale_x_date(date_labels = "%Y", date_breaks = "1 year") # Treat data values as dates.
scale_y_datetime() # Treat data values as datetimes.

# Base color and fill scales (discrete).
scale_fill_brewer(palette = "Blues")
RColorBrewer::display.brewer.all() # To see base color palettes.
scale_fill_grey(start = 0.2, end = 0.8, na.value = "red")

# Base color and fill scales (continuous).
scale_fill_distiller(palette = "Blues")
scale_fill_gradient(low = "red", high = "yellow")

# --- Coordinates --------------------------------------------------------------

# Coordinates set the X and Y limits of the plot.

coord_cartesian(xlim = c(0, 20), ylim = c(40, 80)) # Set X and Y limits.
coord_fixed(ratio = 1/2) # Coordinates with a fixed aspect ratio between X and Y units.

# --- Position Adjustments -----------------------------------------------------

# Position adjustments determine how to arrange geoms that would otherwise
# occupy the same space.

geom_bar(position = "dodge") # Arrange bars that fall on the same data side-by-side.
geom_bar(position = "stack") # Stack elements on top of one another.
geom_bar(position = "fill") # Stack elements on top of one another and normalize height.
geom_label(position = "nudge") # Nudge labels away from points.
geom_point(position = "jitter") # Add random noise to the X and Y position of each element to avoid overplotting.

# --- Themes -------------------------------------------------------------------

theme_bw() # White background with grid lines.
theme_gray() # Gray background with grid lines.
theme_dark() # Dark for contrast.
theme_light() # White background with light grid lines.
theme_classic() # White background with no grid lines.
theme_minimal()
theme_void()

# --- Faceting -----------------------------------------------------------------

# Facets divide a plot into subplots based on the values of one or more discrete
# variables.
facet_grid(cols = vars(fl)) # Facet into columns based on `fl`.
facet_grid(rows = vars(year)) # Facet into rows based on `year`. 
facet_grid(rows = vars(year), cols = vars(fl)) # Facet based on two variables.
facet_wrap(vars(fl)) # Facet wrap into a rectangular format.

# --- Labels and Legends -------------------------------------------------------

# Use labs() to label the elements of your plot.
labs(
    x = "X axis label",
    y = "Y axis label",
    title = "Plot title",
    subtitle = "Subtitle below the title",
    caption = "Caption below the plot",
    alt = "Add alternative text to the plot"
)

# Set the position of label elements.
theme(
    legend.position = "top" # Automatically set legend to the top of the plot.
    legend.position = c(0.9, 0.9), # Manually set legend position.
    plot.title = element_text(hjust = 0.5) # Center plot title.
    legend.text = element_text(size = 7) # Change the size of the legend text.
)

# Place a geom with manually selected aesthetics on the plot.
annotate(geom = "text", x = 8, y = 9, label = "A")

# Set the legend type for a specified aesthetic: colorbar, legend, or none (no legend).
guides(fill = "none")

# Change the size of all text elements.
theme(text = element_text(size = 20))

# Do not render a legend from a specific scale.
scale_colour_manual(name = "", values = c("below" = blue), guide = "none")

# Do not render the legend for a specific geom.
geom_text(aes(label = Ticker), show.legend = FALSE)

# --- Notes on Mapping to Aesthetics -------------------------------------------

# To create a line that is royalblue.
geom_point(aes(y = population), color = "royalblue", size = "blue") 

# Map a line to the key "NYC Population" and then color "NYC Population" royalblue.
geom_line(aes(y = population, color = "NYC Population"), lwd = 1) +
scale_color_manual(values = c("royalblue")) + 

#-------------------------------------------------------------------------------
# ggplot Useful Aesthetics, Geoms, and Scales
#-------------------------------------------------------------------------------

# X-axis date at the yearly frequency.
scale_x_date(date_breaks = "1 year", date_labels = "%Y") +

# Manually scale colors according to an aes key.
geom_line(aes(y = level, color = "below")) +
scale_colour_manual("Direction", values = c("below" = "blue"))

# Colour dots based on a different variable, in this case year.
geom_point(aes(color = year(date)))

# Use markdown (bold, italic) in title or axes.
labs(x = "Axis title with *italics* and **boldface**") +
theme(axis.title.x = ggtext::element_markdown())

# Automatically set scales based on how many tick marks want.
scale_x_continuous(breaks = scales::pretty_breaks(n = 5))

# Left-adjust caption text.
theme(plot.caption = element_text(hjust = 0))

# Create interactive charts and scaling with plotly.
library(plotly)
ggplotly(ggplot())

# Place two plots side-by-side or one above the other.
library(patchwork)
plot1 | plot2 
plot1 / plot2
plot1 | plot2 + plot_layout(guides = "collect") # Removes redundant legends.

# facet_wrap Pagination Solution

# GroupNames = Column used to group data.
# PlotName = Name for the output plot PDFs.
# PlotNrow, PlotNcol = Number of plots to place on each PDF page.
GroupNames <- unique(df$GroupName)
PlotName <- "MyPlot"
PlotNrow <- 4
PlotNcol <- 3

for (i in 1:ceiling(length(GroupNames) / (PlotNrow * PlotNcol))) {

    plot <- ggplot() + 
        # << ggplot code here >> +
        # << ggplot code here >> +
        # << ggplot code here >> +
        ggforce:facet_wrap_paginate(~GroupNames, scaled = "free", nrow = PlotNrow, ncol = PlotNcol, page = i)

    ggsave(
        filename = paste0(PlotName, "_P", i, ".pdf"),
        plot = plot,
        device = "pdf",
        width = 8.5, height = 11, units = "in",
        path = OutputDir
    )
}

#-------------------------------------------------------------------------------
# Stock ggplot Lineplot Example
#-------------------------------------------------------------------------------

plot <- ggplot(data = df, aes(x = date)) +
   
   geom_line(aes(y = temp_outside, colour = "temp_outside"), lwd = 1) +
   geom_line(aes(y = temp_inside, colour = "temp_inside"), lwd = 0.8) +
   
   scale_color_manual(values = c("blue", "red")) + 
   
   labs(
      title = "Outside Versus Inside Temperature",
      x = "Date",
      y = "Temperature",
      colour = NULL
   ) +
   
   coord_cartesian(ylim = c(40, 80)) +
   
   theme_light() +
   theme(legend.position = c(0.15, 0.85))

ggsave(
   filename = "temp_plot.pdf",
   plot = plot,
   device = "pdf",
   width = 9, height = 5, units = "in",
   path = paste0(figs_dir)
)




#===============================================================================
# ggplot2: Elegant Graphics Notes
#===============================================================================

# In the grammar of graphics there are five mapping components:

# layer = collection of geometric elements and statistical transformations. 
# - geoms = (geometric elements) represent what you actually see in the plot:
# points, lines, polygons, etc.
# - stats = summarise the data, for example binning and counting observations to
# create a histogram or fitting a linear model.

# scale = Maps values in data space to values in aesthetic space. This includes
# color, shape, and size.

# coord = Coordinate system, describes how data coordinates are mapped to the 
# plane of the graphic. Alos provides axes and gridlines. 

# facet = Specifies how to break up and display subsets of data. Also known
# as latticing/trellising.

# theme = Controls the finer points of display like font size and background color.

# Graphs are built by adding layers on top of each other.
p <- ggplot(mpg, aes(displ, hwy))    
p + geom_point()

# geom_point() is a shortcut, behind the scenes it calls layer() to create a 
# new layer.
p + layer(
    mapping = NULL,
    data = NULL,
    geom = "point",
    stat = "identity",
    position = "identity"
)

# mapping = set of aesthetic mappings specified using aes()
# data = dataset which overrides the default plot dataset, usually omitted
# geom = name of the geometric object used to draw each observation
# stat = name of the statistical transformation to use, key to histogram, smoothers,
#   binning, etc. To keep the data as is use "identity"
# position = method used to adjust overlapping objects such as jitter or stack. 




#===============================================================================
# Table Notes
#===============================================================================

#-------------------------------------------------------------------------------
# Kable Table Notes
#-------------------------------------------------------------------------------

library(kableExtra)

# Needs to be rendered with Pandoc in an Rmarkdown document or using `tex2pdf` 
# and pdflatex.

TableData %>%

    # --- General Settings ---
    knitr::kable(
        format = "latex",
        align = "lcr", # Align column 1 left, column two center, column 3 right.
        booktabs = TRUE, # Improves aesthetics.
        longtable = TRUE, # Improves aesthetics.
        linesep = "",
        col.names = c("Colname 1", "Colname 2", "Colname 3"), # Option to set custom column names.
        digits = 1 # Option to round all digits.
    ) %>% 

    # --- Spacing and Alignment ---
    kable_styling(full_width = T) %>% # Spread columns across the width of the full page.
    column_spec(1:3, width = "2cm") %>% # Set the widths of specific columns.
    kable_styling(position = "center") %>% # Align table to the center or the left of the page.
    add_indent(c(1, 3, 5)) %>% # Indent specific rows.

    # --- General Styling
    collapse_rows(columns = c(1, 2), latex_hline = "major") %>% # Collapse repeated values to one row.
    add_header_above(c(" " = 2, "Header" = 3)) %>% # Adds no header over the first two columns and a custom header over the third column.
    kable_styling(latex_options = c("hold_position", "repeat_header")) %>%
    kable_styling(latex_options = "striped") %>% # Stripe every other row.
    kable_styling(latex_options = "striped", stripe_index = c(1,2, 5:6)) %>% # Stripe specific rows.
    kable_styling(latex_options = "striped", stripe_index = which(df$Marker == "X"), stripe_color = "gray") %>% # Strip based on a condition.

    # --- Font Size, Colors, Aesthetics ---
    kable_styling(font_size = 7) %>% # Set table font size.
    row_spec(c(1, 2), color = "red") %>% # Color the text of the first two rows red.
    column_spec(2, bold = TRUE, color = spec_color(mtcars$mpg[1:8])) %>% # Color values in a column according to magnitude.
    cell_spec(vs_dt[[5]], color = "white", bold = T, # Color specific cells according to magnitude.
        background = spec_color(1:10, end = 0.9, option = "A", direction = -1)) %>%

    # --- Footnotes ---
    footnote(
        general = "Here is a general comments of the table. ", # General footnote.
        number = c("Footnote 1; ", "Footnote 2; ") # Numbered footnotes.
    ) 
    
# Save a kable table to .tex output. 
save_kable(filepath)

#-------------------------------------------------------------------------------
# Stargazer and tex2pdf Notes
#-------------------------------------------------------------------------------

# Example, creating a numeric latex table with stargazer and tex2pdf.
table <- data.frame(c(1, 2, 3), c(4, 5, 6))

stargazer(
   table,
   summary = FALSE,
   title = "Demo Table",
   notes = c(
      "Demo Note 1.",
      "Demo Note 2."
   ),
   out = paste0(dir, "demo_note.tex")
)

tex2pdf(
   texfiles = paste0(dir, "demo_note.tex"),
   filename = paste0(dir, "demo_note_out.pdf"),
   directory = dir,
   horizontal = FALSE
)

# Latex code to make a column span multiple lines.
# \begin{tabular}{@{}@c@{}} R-squared \\ GDP Growth \end{tabular}

# Latex code to make a column span multiple columns.
# \multicolumn{2}{c}{This column name spans two columns.}
